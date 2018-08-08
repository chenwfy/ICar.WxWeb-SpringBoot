package com.mycarx.wxweb.web.service

import com.mycarx.wxweb.remoteApi.weChat.UserFromServerApi
import com.mycarx.wxweb.web.domain.coupon.*
import com.mycarx.wxweb.web.domain.user.*
import com.mycarx.wxweb.web.domain.weChat.UserFromServerResult
import com.mycarx.wxweb.web.repository.JdbcTemplateRepository
import com.mycarx.wxweb.web.repository.coupon.UserOilCouponRepository
import com.mycarx.wxweb.web.repository.coupon.UserWashCouponRepository
import com.mycarx.wxweb.web.repository.user.CarOwnerRepository
import com.mycarx.wxweb.web.repository.user.UserCarRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * 用户相关部分业务逻辑处理
 */
@Service
class UserService {
    @Autowired
    private lateinit var carOwnerRepository: CarOwnerRepository
    @Autowired
    private lateinit var userOilCouponRepository: UserOilCouponRepository
    @Autowired
    private lateinit var userWashCouponRepository: UserWashCouponRepository
    @Autowired
    private lateinit var userCarRepository: UserCarRepository
    @Autowired
    private lateinit var couponService: CouponService
    @Autowired
    private lateinit var jdbcTemplateRepository: JdbcTemplateRepository

    /**
     * 用户登录
     */
    fun userLogin(userLoginForm: LoginForm): CarOwnerEntity? {
        try {
            val userEntity: CarOwnerEntity? = carOwnerRepository.findUserByOpenId(userLoginForm.openId)
            if (null != userEntity) {
                //用户已存在，更新头像和昵称
                if (!userEntity.nickname.equals(userLoginForm.nickname, true) || userEntity.headImg.equals(userLoginForm.headImg, true)) {
                    userEntity.nickname = userLoginForm.nickname
                    userEntity.headImg = userLoginForm.headImg

                    carOwnerRepository.save(userEntity)
                }

                return userEntity
            }

            //用户不存在，注册新用户
            val newUserEntity = CarOwnerEntity()
            newUserEntity.cityId = ""
            newUserEntity.registerDate = Date()
            newUserEntity.name = ""
            newUserEntity.mobile = ""
            newUserEntity.gender = userLoginForm.gender
            newUserEntity.area = ""
            newUserEntity.openId = userLoginForm.openId
            newUserEntity.unionId = userLoginForm.unionId
            newUserEntity.nickname = userLoginForm.nickname
            newUserEntity.headImg = userLoginForm.headImg
            newUserEntity.isFollow = 0
            newUserEntity.brandId = 0L
            newUserEntity.oilsTypeId = 0L
            newUserEntity.level = UserLevel.Ordinary
            newUserEntity.point = 0
            newUserEntity.followed = 0
            carOwnerRepository.save(newUserEntity)
            return newUserEntity
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 用户关注公众号
     * 返回：是否首次关注
     */
    fun userSubscribe(wxUserInfo: UserFromServerResult): Pair<Boolean, Long> {
        val userEntity: CarOwnerEntity? = carOwnerRepository.findUserByOpenId(wxUserInfo.openId)
        if (null != userEntity) {
            userEntity.isFollow = (userEntity.isFollow + 1).toShort()
            val firstFollow: Boolean = (userEntity.followed ?: 0) == 0.toShort()
            if (firstFollow) {
                userEntity.followed = 1
            }
            carOwnerRepository.save(userEntity)

            return Pair(firstFollow, userEntity.id)
        }

        //如果用户不存在，直接注册
        try {
            val newUserEntity = CarOwnerEntity()
            newUserEntity.cityId = ""
            newUserEntity.registerDate = Date()
            newUserEntity.name = ""
            newUserEntity.mobile = ""
            newUserEntity.gender = if (wxUserInfo.sex == 2) "女" else "男"
            newUserEntity.area = ""
            newUserEntity.openId = wxUserInfo.openId
            newUserEntity.unionId = wxUserInfo.unionId ?: ""
            newUserEntity.nickname = wxUserInfo.nickName
            newUserEntity.headImg = wxUserInfo.headImageUrl
            newUserEntity.isFollow = 1
            newUserEntity.brandId = 0L
            newUserEntity.oilsTypeId = 0L
            newUserEntity.level = UserLevel.Ordinary
            newUserEntity.point = 0
            newUserEntity.followed = 1
            carOwnerRepository.save(newUserEntity)

            return Pair(true, newUserEntity.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(false, 0L)
    }

    /**
     * 用户取消关注公众号
     */
    fun userUnSubscribe(wxUserInfo: UserFromServerResult) {
        val userEntity: CarOwnerEntity? = carOwnerRepository.findUserByOpenId(wxUserInfo.openId)
        if (null != userEntity && userEntity.isFollow > 1) {
            userEntity.isFollow = (userEntity.isFollow - 1).toShort()
            carOwnerRepository.save(userEntity)
        }
    }

    /**
     * 更新用户是否关注状态
     */
    fun updateUserBySubscribeStatus(openId: String) {
        //调用微信接口获取用户信息
        val wxUserInfo: UserFromServerResult? = UserFromServerApi(openId).execute()
        if (null != wxUserInfo) {
            //获取用户在系统中的信息
            val userEntity: CarOwnerEntity? = carOwnerRepository.findUserByOpenId(openId)
            if (null != userEntity) {
                //用户已取消关注
                if (wxUserInfo.subscribe == 0) {
                    //有openid记录
                    if (this.queryOpenIdExists(openId)) {
                        this.removeOpenId(openId)
                        if (userEntity.isFollow > 1) {
                            userEntity.isFollow = (userEntity.isFollow - 1).toShort()
                            carOwnerRepository.save(userEntity)
                        }
                    }
                }

                //用户已经关注
                if (wxUserInfo.subscribe == 1) {
                    //如果不存在openid记录，则新增
                    if (!this.queryOpenIdExists(openId)) {
                        this.insertOpenId(UserOpenId(userEntity.id, openId))
                        userEntity.isFollow = (userEntity.isFollow + 1).toShort()
                    }

                    //用户是否从未关注过
                    val firstFollow: Boolean = (userEntity.followed ?: 0) == 0.toShort()
                    if (firstFollow) {
                        userEntity.followed = 1
                    }
                    carOwnerRepository.save(userEntity)

                    //首次关注送优惠券
                    if (firstFollow) {
                        couponService.presentedCouponToUser(userEntity.id, CouponConfigAction.FirstFollow)
                    }
                }
            }
        }
    }

    /**
     * 查询OPENID是否存在
     */
    fun queryOpenIdExists(openId: String): Boolean {
        return jdbcTemplateRepository.queryOpenIdExists(openId)
    }

    /**
     * 写入OPENID
     */
    fun insertOpenId(userOpenId: UserOpenId) {
        jdbcTemplateRepository.insertOpenId(userOpenId)
    }

    /**
     * 删除openId
     */
    fun removeOpenId(openId: String) {
        jdbcTemplateRepository.removeOpenId(openId)
    }

    /**
     * 获取用户信息
     */
    fun queryUserById(userId: Long): CarOwnerEntity? {
        return carOwnerRepository.findUserById(userId)
    }

    /**
     * 更新用户最近一次加油的油号和品牌
     */
    fun updateUserOilTypeAndBrand(userId: Long, oilTypeId: Long, brandId: Long) {
        val user: CarOwnerEntity? = this.queryUserById(userId)
        if (null != user) {
            if (user.oilsTypeId != oilTypeId || user.brandId != brandId) {
                carOwnerRepository.updateUserOilTypeAndBrand(userId, oilTypeId, brandId)
            }
        }
    }

    /**
     * 查询手机号码是否已存在
     */
    fun mobileIsExists(userId: Long, mobile: String): Boolean {
        return carOwnerRepository.queryMobileCount(userId, mobile) > 0
    }

    /**
     * 设置用户手机号码（首次绑定专用）
     */
    fun setUserMobile(userId: Long, mobile: String): Boolean {
        try {
            //修改用户手机号
            carOwnerRepository.updateUserMobile(userId, mobile)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 修改用户手机号码
     */
    fun changeUserMobile(userId: Long, mobile: String) {
        carOwnerRepository.updateUserMobile(userId, mobile)
    }

    /**
     * 获取指定用户所有车辆信息
     */
    fun queryAllCarListByUserId(userId: Long): List<UserCarEntity> {
        return userCarRepository.findAllListByUserId(userId)
    }

    /**
     * 新建用户车辆信息列表
     */
    fun userCreateNewCar(carInfo: UserCarEntity) {
        userCarRepository.save(carInfo)
    }

    /**
     * 删除车辆信息
     */
    fun deleteUserCar(userId: Long, carId: Long) {
        userCarRepository.deleteUserCar(userId, carId)
    }

    /**
     * 获取指定用户加油支付时可用的加油券列表
     * 条件：未过期、未使用，且按照过期时间升序排列（即即将过期的排在最前面）
     */
    fun queryUserOilCouponsForAddOil(userId: Long): List<UserOilCouponEntity> {
        return userOilCouponRepository.queryUserOilCouponsForAddOil(userId, Date())
    }

    /**
     * 获取指定用户符合筛选条件的加油券总数
     */
    fun queryUserOilCouponsCount(userId: Long, filter: String, dateNow: Date, expiredStart: Date): Int {
        return jdbcTemplateRepository.queryUserOilCouponsCount(userId, filter, dateNow, expiredStart)
    }

    /**
     * 获取指定用户符合筛选条件的加油券分页列表
     */
    fun queryUserOilCouponPaging(userId: Long, filter: String, dateNow: Date, expiredStart: Date, pageSize: Int, pageIndex: Int): List<UserOilCouponEntity> {
        return jdbcTemplateRepository.queryUserOilCouponPaging(userId, filter, dateNow, expiredStart, pageSize, pageIndex)
    }

    /**
     * 用户兑换新的加油券
     */
    @Transactional(rollbackFor = [Exception::class])
    fun userRedeemNewOilCoupon(userId: Long, code: String): Pair<Boolean, String> {
        try {
            val coupon: UserOilCouponEntity? = userOilCouponRepository.queryUserOilCouponsForUpdateByCheckCode(code)
            if (null != coupon) {
                val dateNow = Date()
                if (coupon.expiredDate <= dateNow) {
                    return Pair(false, "兑换码已过期！")
                }

                if (coupon.userId > 0L || coupon.state != CouponState.NotActive) {
                    return Pair(false, "兑换码不能重复兑换哦！")
                }

                coupon.userId = userId
                coupon.state = CouponState.NotUse
                coupon.activeDate = dateNow
                userOilCouponRepository.save(coupon)
                return Pair(true, "兑换成功！")
            }
            return Pair(false, "兑换码输入错误！")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(false, "兑换失败！")
    }

    /**
     * 获取指定用户、指定ID的加油券详情
     */
    fun queryUserOilCouponDetail(userId: Long, couponId: Long): UserOilCouponEntity? {
        return userOilCouponRepository.queryUserOilCouponDetail(userId, couponId)
    }

    /**
     * 获取指定用户洗车支付时可用的洗车券列表
     * 条件：未过期、未使用，且按照过期时间升序排列（即即将过期的排在最前面）
     */
    fun queryUserWashCouponsForAddOil(userId: Long): List<UserWashCouponEntity> {
        return userWashCouponRepository.queryUserOilCouponsForAddOil(userId, Date())
    }

    /**
     * 获取指定用户符合筛选条件的洗车券总数
     */
    fun queryUserWashCouponsCount(userId: Long, filter: String, dateNow: Date, expiredStart: Date): Int {
        return jdbcTemplateRepository.queryUserWashCouponsCount(userId, filter, dateNow, expiredStart)
    }

    /**
     * 获取指定用户符合筛选条件的洗车券分页列表
     */
    fun queryUserWashCouponPaging(userId: Long, filter: String, dateNow: Date, expiredStart: Date, pageSize: Int, pageIndex: Int): List<UserWashCouponEntity> {
        return jdbcTemplateRepository.queryUserWashCouponPaging(userId, filter, dateNow, expiredStart, pageSize, pageIndex)
    }

    /**
     * 用户兑换新的洗车券
     */
    @Transactional(rollbackFor = [Exception::class])
    fun userRedeemNewWashCoupon(userId: Long, code: String): Pair<Boolean, String> {
        try {
            val coupon: UserWashCouponEntity? = userWashCouponRepository.queryUserWashCouponsForUpdateByCheckCode(code)
            if (null != coupon) {
                val dateNow = Date()
                if (coupon.expiredDate <= dateNow) {
                    return Pair(false, "兑换码已过期！")
                }

                if (coupon.userId > 0L || coupon.state != CouponState.NotActive) {
                    return Pair(false, "兑换码不能重复兑换哦！")
                }

                coupon.userId = userId
                coupon.state = CouponState.NotUse
                coupon.activeDate = dateNow
                userWashCouponRepository.save(coupon)
                return Pair(true, "兑换成功！")
            }

            return Pair(false, "兑换码输入错误！")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(false, "兑换失败！")
    }

    /**
     * 获取指定用户、指定ID的洗车券详情
     */
    fun queryUserWashCouponDetail(userId: Long, couponId: Long): UserWashCouponEntity? {
        return userWashCouponRepository.queryUserWashCouponDetail(userId, couponId)
    }

    /**
     * 获取用户指定类型的消费记录总数
     */
    fun queryUserSimpleOrderCount(userId: Long, orderType: String): Int {
        return jdbcTemplateRepository.queryUserSimpleOrderCount(userId, orderType)
    }

    /**
     * 获取用户指定类型的消费记录分页列表
     */
    fun queryUserSimpleOrderPaging(userId: Long, orderType: String, pageSize: Int, pageIndex: Int): List<SimpleOrderItem> {
        return jdbcTemplateRepository.queryUserSimpleOrderPaging(userId, orderType, pageSize, pageIndex)
    }

    /**
     * 查询订单详情
     */
    fun queryUserSimpleOrderDetail(orderNo: String): SimpleOrderItem? {
        return jdbcTemplateRepository.queryUserSimpleOrderDetail(orderNo)
    }
}
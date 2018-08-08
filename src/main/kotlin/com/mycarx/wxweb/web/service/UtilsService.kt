package com.mycarx.wxweb.web.service

import com.mycarx.wxweb.web.domain.common.KVPair
import com.mycarx.wxweb.web.domain.common.StationType
import com.mycarx.wxweb.web.domain.support.CityEntity
import com.mycarx.wxweb.web.domain.support.WeiXinAccountEntity
import com.mycarx.wxweb.web.repository.support.CityRepository
import com.mycarx.wxweb.web.repository.support.WeiXinAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UtilsService {
    /**
     * 静态编译方法部分
     */
    companion object {
        /**
         * 组装距离筛选条件
         */
        @JvmStatic
        fun buildRangeBands(): List<KVPair<Long, String>> {
            return mutableListOf(
                    KVPair<Long, String>(0, "不限距离"),
                    KVPair<Long, String>(1, "一公里内"),
                    KVPair<Long, String>(3, "三公里内"),
                    KVPair<Long, String>(5, "五公里内"),
                    KVPair<Long, String>(10, "十公里内"),
                    KVPair<Long, String>(20, "二十公里内"),
                    KVPair<Long, String>(50, "五十公里内")
            )
        }
    }

    /**
     * 实例方法部分
     */
    @Autowired
    private lateinit var cityRepository: CityRepository
    @Autowired
    private lateinit var weiXinAccountRepository: WeiXinAccountRepository

    /**
     * 查询所有微信公众号配置
     */
    fun findAllWeiXinAccountList(): List<WeiXinAccountEntity> {
        return weiXinAccountRepository.findAll()
    }

    /**
     * 组装加油站可用的城市筛选项
     */
    fun buildStationCity(stationType: StationType = StationType.GasStation): List<KVPair<String, String>> {
        val resultList = mutableListOf(KVPair("", "不限"))
        this.queryEnabledCityList(stationType).forEach { resultList.add(KVPair(it.cityId, it.cityName)) }
        return resultList
    }

    /**
     * 查询城市列表
     */
    private fun queryEnabledCityList(stationType: StationType): List<CityEntity> {
        return when (stationType) {
            StationType.GasStation -> cityRepository.queryEnabledCityForOilStation()
            else -> cityRepository.queryEnabledCityForWashCarStation()
        }
    }
}
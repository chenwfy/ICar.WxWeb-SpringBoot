package com.mycarx.wxweb.web.repository

import com.mycarx.wxweb.web.domain.common.OrderPayType
import com.mycarx.wxweb.web.domain.coupon.CouponState
import com.mycarx.wxweb.web.domain.coupon.UserOilCouponEntity
import com.mycarx.wxweb.web.domain.coupon.UserWashCouponEntity
import com.mycarx.wxweb.web.domain.oil.*
import com.mycarx.wxweb.web.domain.user.SimpleOrderItem
import com.mycarx.wxweb.web.domain.user.UserOpenId
import com.mycarx.wxweb.web.domain.wash.WashStation
import com.mycarx.wxweb.web.domain.wash.WashStationListFilter
import com.mycarx.wxweb.web.domain.wash.WashStationStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet
import java.util.*

/**
 * JdbcTemplate原生查询数据层
 */
@Service
class JdbcTemplateRepository {
    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate
    @Autowired
    private lateinit var namedJdbcTemplate: NamedParameterJdbcTemplate
    @Autowired
    private lateinit var transactionTemplate: TransactionTemplate
}
/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 2016-11-22 - Create By peiwang
 */

package com.zpayment.cmn.exp;

/**
 * 错误码定义，公共基础类错误码，范围0001 ~ 0099
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class BaseErrorCode {

	public static final String FAIL = "0";

	public static final String SUCCEED = "1";

	public static final String COMM_HTTP_GET_FAILED = "0108"; // CMN-从URL {0}  GET数据失败
	public static final String COMM_HTTP_POST_FAILED = "0109"; // CMN-向URL{0} POST数据失败
	public static final String COMM_HTTP_STATUS_ERROR = "0110"; // CMN-HTTP返回应答码{0}
	/** 0001 ~ 0005 未归类异常 */
	public static final String COMN_UNKOWN_EXCEPTION = "1001"; // CMN - 未知异常
	public static final String COMN_NULL_EXCEPTION = "1002"; // CMN - 空指针异常
	public static final String COMN_EXCEPTION_TYPE_ERROR = "1003"; // CMN -  异常类型错误
	public static final String COMN_REFLACT_FAIL = "1004"; // CMN - 反射错误

	/** 0006 ~ 0010 JNDI异常 */
	public static final String COMN_JNDI_SERVICE_NOT_EXIST = "1006"; // CMN - JNDI服务不存在

	/** 0010 ~ 0019 数据库异常 */
	public static final String COMN_DB_UNKOWN_EXCEPTION = "1010"; // CMN - 数据库访问时出现的未知异常，除下述异常外的异常
	public static final String COMN_DB_SQL_EXCEPTION = "1011"; // CMN - 执行JDBC SQL语句或JPAJPQL语句时异常
	public static final String COMN_DB_RECORD_EXISTED = "1012"; // CMN - 插入数据时，数据已存在异常，JDBC与JPA均可用
	public static final String COMN_DB_RECORD_NOT_EXIST = "1013"; // CMN - 更新时，数据已被删除异常，仅JPA可用。JDBC执行方法会返回执行成功的记录数，在调用时判断处理。
	public static final String COMN_DATA_TYPE_INVALID = "1014"; // CMN - 查询结果集合中的数据类型转换错误
	public static final String COMN_DATA_MAPPING_EXCEPTOIN = "1015"; // CMN - 查询结果集合映射到Java对象错误
	public static final String COMN_DATA_TYPE_MIS_MATCH = "1016";// CMN - 数据不合法
	public static final String COMN_DATA_INVALID_FOMAT_TYPE = "1017"; // CMN - 数据类型不正确
	public static final String COMN_DATA_INVALID_FOMAT_ARG_CNT = "1018"; // CMN - SQL参数个数不正确
	public static final String COMN_DATA_INVALID_FOMAT_VALUE = "1019"; // CMN - SQL参数格式不正确
	public static final String COMM_DB_TABLE_METADATA_EXCEPTOIN = "1020"; // CMN - 获取数据库元信息出错

	/** 0020 ~ 0029 文件IO相关异常 */
	public static final String COMN_FILE_NOT_EXIST = "1020"; // CMN - 文件不存在
	public static final String COMN_FILE_IO_ERROR = "1021"; // CMN - 文件读写异常
	public static final String COMN_FILE_ENCODE_ERROR = "1022"; // CMN - 文件编码错误
	public static final String COMN_FILE_CREATE_ERROR = "1023"; // CMN - 创建文件或目录错误
	public static final String COMN_FILE_DELETE_ERROR = "1024"; // CMN - 删除文件或目录错误
	public static final String COMN_FILE_NO_PERMISSION = "1025"; // CMN - 无文件操作权限
	public static final String COMN_FILE_PATH_INVALID = "1026"; // CMN - 文件路径错误

	/** 0030 ~ 0039 XML等解析异常 */
	public static final String COMN_XML_PARSE_FAILED = "1035"; // CMN - XML格式化错误
	public static final String COMN_XML_WRITE_FAILED = "1036"; // CMN - XML写错误

	/** 0040 ~ 0049 缓存异常 */
	public static final String COMN_CACHE_DESCRIPT_INVALID = "1040"; // CMN - 缓存定义错误
	public static final String COMN_CACHE_DEPENDENCE_ERROR = "1041"; // CMN - 缓存依赖错误
	public static final String COMN_CACHE_TYPE_INVALID = "1042"; // CMN - 缓存类型错误
	public static final String COMN_CACHE_STATUS_ERROR = "1043"; // CMN - 缓存状态不正确，不能进行操作
	public static final String COMN_CACHE_INIT_FAILED = "1044"; // CMN - 缓存初始化失败
	public static final String COMN_CACHE_LOAD_FAILED = "1045"; // CMN - 缓存加载失败
	public static final String COMN_CACHE_DESTROY_FAILED = "1046"; // CMN - 缓存销毁失败
	public static final String COMN_CACHE_KEY_INVALID = "1047"; // CMN - 缓存主键错误
	public static final String COMN_CACHE_NOT_EXIST = "1048"; // CMN - 缓存不存在
	public static final String COMN_CACHE_KEY_DUPLICATE = "1049"; // CACHE - 缓存重复

	/** 0050 ~ 0059 JMS消息异常 */
	public static final String COMN_JMS_MESSAGE_SEND_FAILED = "1050"; // CMN - JMS消息发送异常
	public static final String COMN_JMS_MESSAGE_QUEUE_JNDI_NOT_EXIST = "1051"; // CMN - JMS消息队列JNDI查找错误
	public static final String COMN_JMS_MESSAGE_QUEUECONN_JNDI_NOT_EXIST = "1052"; // CMN - JMS消息队列连接工厂JNDI查找错误

	/** 0060 ~ 0069 socket通信异常 */
	public static final String COMN_LINK_UNKOWN_ERROR = "1060"; // CMN - 通信异常
	public static final String COMN_LINK_DIS_CONNECT = "1061"; // CMN - 通信链路未连接异常
	public static final String COMN_LINK_SEND_ERROR = "1062"; // CMN - 发送消息异常
	public static final String COMN_LINK_TO_BYTES_ERROR = "1063"; // CMN - 转换报文错误
	public static final String COMN_LINK_PARSE_BYTES_ERROR = "1064"; // CMN - 解析报文错误
	public static final String COMN_LINK_TIMEOUT = "1065"; // CMN - 响应超时
	public static final String COMN_SEND_QUEUE_OVERLOAD = "1067"; // CMN - 发送队列超限异常
	public static final String COMN_LINK_CONFIG_ERROR = "1068"; // CMN - 通信配置错误

	/** 命令执行 相关 */
	public static final String COMN_CMD_NO_PERMISSION = "1071"; // CMN - 无执行权限
	public static final String COMN_CMD_EXEC_FAILEd = "1072"; // CMN - 执行失败

	/** 0090 ~ 0099 其他异常 */
	public static final String COMN_SEQUENCE_ID_OVERLOAD = "1090"; // CMN - seq超出限制
	/** 存在其他正在执行的操作 */
	public static final String COMN_OPERATION_LOCKED = "1091"; // CMN - 操作正在进行，不能再次发起

	public static final String COMN_INVALID_PROPERTY = "1092"; // CMN - 错误的配置项

}

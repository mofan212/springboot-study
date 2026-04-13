-- ----------------------------
-- Table structure for biz_data
-- ----------------------------
DROP TABLE IF EXISTS `biz_data`;
CREATE TABLE `biz_data`
(
    `id`            bigint(20) NOT NULL,
    `business_code` varchar(255),
    PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for rt_log
-- ----------------------------
DROP TABLE IF EXISTS `rt_log`;
CREATE TABLE `rt_log`
(
    `id`          bigint(20) NOT NULL,
    `log_content` varchar(255),
    PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for tb_student
-- ----------------------------
DROP TABLE IF EXISTS `tb_student`;
CREATE TABLE `tb_student`
(
    `id`     bigint(20) NOT NULL,
    `name`   varchar(25),
    `age`    tinyint(4),
    `gender` tinyint(4),
    PRIMARY KEY (`id`)
);

-- ----------------------------
-- Table structure for tb_test_null
-- ----------------------------
DROP TABLE IF EXISTS `tb_test_null`;
CREATE TABLE `tb_test_null`
(
    `id`          bigint(20) NOT NULL,
    `name`        varchar(20) NOT NULL,
    `age`         int(11),
    `empty_field` varchar(20),
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_name`(`name`)
);
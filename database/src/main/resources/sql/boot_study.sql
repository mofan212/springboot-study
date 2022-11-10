SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_student
-- ----------------------------
DROP TABLE IF EXISTS `tb_student`;
CREATE TABLE `tb_student`
(
    `id`     bigint(20) NULL DEFAULT NULL,
    `name`   varchar(25) NULL DEFAULT NULL,
    `age`    tinyint(4) NULL DEFAULT NULL,
    `gender` tinyint(4) NULL DEFAULT NULL
)

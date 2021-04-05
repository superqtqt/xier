DROP table if exists demo;
CREATE TABLE `demo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `name` varchar(128) NOT NULL COMMENT '名称'
  PRIMARY KEY (`id`)
);
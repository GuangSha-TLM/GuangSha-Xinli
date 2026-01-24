package com.example.demo.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("student_info")
public class StudentInfo extends BaseEntity {

//	@TableId(type = IdType.AUTO)
//	private Long id;

	@TableField("user_id")
	private Long userId;
	@TableField("grade")
	private String grade;
	@TableField("major")
	private String major;
	@TableField("college")
	private String college;

	@TableField("classname")
	private String className;
}

package com.example.demo.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.demo.domain.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("student_info")
public class StudentInfo extends BaseEntity {

	@TableId(type = IdType.AUTO)
	private Long id;

	@TableField("user_id")
	private Long userId;

	private String grade;
	private String major;
	private String collage;
	private String className;
}

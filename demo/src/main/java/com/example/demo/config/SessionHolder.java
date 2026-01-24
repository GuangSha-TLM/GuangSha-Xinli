package com.example.demo.config;

import com.example.demo.domain.dto.User;

import javax.servlet.http.HttpSession;

public class SessionHolder {

	public static User getSession(HttpSession session){

		if (session == null){
			return null;
		}

		Object object = session.getAttribute("user");
		return object instanceof User ? (User) object : null;
	}
}

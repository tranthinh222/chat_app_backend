package com.thinhtran.chatapp.controller;

import com.sun.security.auth.UserPrincipal;
import com.thinhtran.chatapp.domain.response.ResMessageFullDto;
import com.thinhtran.chatapp.domain.response.ResultPaginationDto;
import com.thinhtran.chatapp.service.MessageService;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.annotation.ApiMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1")
public class MessageController {

}

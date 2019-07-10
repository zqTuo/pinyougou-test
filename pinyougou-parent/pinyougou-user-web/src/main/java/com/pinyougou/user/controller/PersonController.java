package com.pinyougou.user.controller;

import com.pinyougou.domain.Error;
import com.pinyougou.domain.Result;
import com.pinyougou.user.model.Person;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/person")
@RestController
public class PersonController {
    @RequestMapping("/add")
    public Result add(@Valid @RequestBody Person person, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Result result = new Result();
            for (FieldError fieldError : fieldErrors) {
                Error error = new Error(fieldError.getField(),fieldError.getDefaultMessage());
                result.getErrorList().add(error);
                System.out.println("错误信息》》》》"+fieldError.getDefaultMessage());
            }
            return  result;
        }else {
            return new Result(true,"meicuo");
        }
    }
}

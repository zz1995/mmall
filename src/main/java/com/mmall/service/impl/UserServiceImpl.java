package com.mmall.service.impl;

        import com.mmall.common.*;
        import com.mmall.dao.UserMapper;
        import com.mmall.pojo.User;
        import com.mmall.service.IUserService;
        import com.mmall.util.MD5Util;
        import org.apache.commons.lang3.StringUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.UUID;

/**
 * Created by 12456 on 2017/5/26.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("用户不存在!");
        }
        // 密码MD5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("密码错误!");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功!",user);
    }

    @Override
    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);

        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败!");
        }
        return ServerResponse.createBySuccessMessage("注册成功!");

    }
    //校验
    public ServerResponse<String> checkValid(String str,String type){
        if (StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUserName(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("用户已存在!");
                }

                if (Const.EMAIL.equals(type)) {
                    resultCount = userMapper.checkEmail(str);
                    if(resultCount > 0){
                        return ServerResponse.createByErrorMessage("email已存在!");
                    }
                }
            }

        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验通过");
    }
    //找回密码，根据问题找回
    public ServerResponse<String> selectQuestion(String username){
        ServerResponse response = this.checkValid(username,Const.USERNAME);
        if(response.isSuccess()){
            ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("需要找回的密码问题为空");
    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount > 0){//大于0说明数据库中这个用户的问题及答案正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token未传递");
        }
        ServerResponse response = this.checkValid(username,Const.USERNAME);
        if(response.isSuccess()){
            ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token失效");
        }
        if(StringUtils.equals(forgetToken,token)){
            String MD5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,MD5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }else {
                return ServerResponse.createByErrorMessage("token错误,请重新获取");
            }
        }
            return ServerResponse.createByErrorMessage("修改密码失败");

    }
}

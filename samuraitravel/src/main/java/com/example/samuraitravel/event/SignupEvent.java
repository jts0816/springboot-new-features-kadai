package com.example.samuraitravel.event;

import org.springframework.context.ApplicationEvent;

import com.example.samuraitravel.entity.User;

import lombok.Getter;

// * Event : Listenerクラスにイベントが発生したことを知らせるクラス。また、イベントに関する情報（会員登録したユーザーなど）も保持できる。
// * 一般的にはApplicationEventクラスを継承して作成する。
@Getter
public class SignupEvent extends ApplicationEvent {
    private User user;
    private String requestUrl;        

    // * イベントに関する情報を保持（リクエストを受けたURL、登録したユーザーそのもの）
    public SignupEvent(Object source, User user, String requestUrl) {
        super(source);
        
        this.user = user;        
        this.requestUrl = requestUrl;
    }
}
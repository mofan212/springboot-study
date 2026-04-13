package indi.mofan.service;


import org.springframework.stereotype.Service;

/**
 * 模拟一个容易出现报错的外部调用服务
 *
 * @author mofan
 * @date 2026/4/13 11:22
 */
@Service
public class ExternalCallService {
    public void executeError() {
        throw new RuntimeException("ExternalCallServiceImpl simulated error");
    }
}

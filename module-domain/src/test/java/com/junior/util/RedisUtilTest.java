package com.junior.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;


//TODO: @Value의 값을 주입하는 방법 변경

@SpringBootTest(properties = {"spring.data.redis.port=6379", "spring.data.redis.host=localhost"})
class RedisUtilTest {


    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedisUtil redisUtil;


    @AfterEach
    void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @DisplayName("Redis에 데이터를 저장한 후 key에 해당하는 값을 정상적으로 불러와야 함")
    void save_and_get_data() {

        //given
        String key = "key";
        String value = "value";

        //when
        redisUtil.setData(key, value);

        //then
        assertThat(redisUtil.getData(key)).isEqualTo(value);

    }


    //TODO: 만료 시간 테스트에 대해 좀 더 나이스한 방법 찾기
    @Test
    @DisplayName("Redis에 데이터가 정상적으로 저장되고, 특정 시간이 지나면 만료되어 조회할 수 없어야 함")
    void setDataExpire() throws InterruptedException {

        //given
        String key = "key";
        String value = "value";

        //when
        redisUtil.setDataExpire(key, value, 1);
        assertThat(redisUtil.getData(key)).isEqualTo(value);
        Thread.sleep(1001);

        assertThat(redisUtil.getData(key)).isNull();

    }

    @Test
    @DisplayName("Redis에 존재하는 데이터에 대해 true를, 그렇지 않다면 false를 반환해야 함")
    void existsByKey() {

        //given
        String key = "key";
        String falseKey = "ke";
        String value = "value";

        //when
        redisUtil.setData(key, value);

        //then
        assertThat(redisUtil.existsByKey(key)).isTrue();
        assertThat(redisUtil.existsByKey(falseKey)).isFalse();
    }

    @Test
    @DisplayName("삭제된 데이터는 조회할 수 없어야 함")
    void deleteData() {
        //given
        String key = "key";
        String value = "value";

        redisUtil.setData(key, value);
        assertThat(redisUtil.getData(key)).isEqualTo(value);

        //when
        redisUtil.deleteData(key);

        //then
        assertThat(redisUtil.getData(key)).isNull();
    }
}
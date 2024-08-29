package com.example.demo.mock;

import com.example.demo.common.service.ClockHolder;
import lombok.RequiredArgsConstructor;

import java.time.Clock;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {

    private final long mills;

    @Override
    public long mills() {
        return mills;
    }
}

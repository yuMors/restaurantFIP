package com.aurora.strategy.impl;

import com.aurora.strategy.UploadStrategy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {


    public abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

}

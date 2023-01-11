package com.imshabr;

import com.imshabr.parser.HabrParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImsHabr {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImsHabr.class);

    public static void main(final String[] args) throws Exception {
        HabrParser habrParser = new HabrParser(args[0]);
        habrParser.readFeed();
    }
}

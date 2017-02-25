package com.hasset.umovies.utils;

/**
 * Created by biniamasnake on 14/02/2017.
 */

public class Util {

    public static String constructImagePath(String posterPath) {
        return "http://image.tmdb.org/t/p/w780".concat(posterPath);
    }
}

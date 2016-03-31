package com.hcentive.cloudmanage.utils;

import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	public static <E> List<E> makeCollection(Iterable<E> iter) {
	    List<E> list = new ArrayList<E>();
	    for (E item : iter) {
	        list.add(item);
	    }
	    return list;
	}

}

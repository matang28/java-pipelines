package com.matangolan.pipelines.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Matan on 06-Nov-16.

 */
public class ListUtils {

    public static Boolean isEmpty(List list) {

        if (null==list)
            return true;

        return list.size() == 0;

    }

    public static Boolean notEmpty(List list) {
        return !isEmpty(list);
    }

    public static <T> String toDelimitedString(List<T> list, String delimiter, String prefix, String postfix) {

        if (notEmpty(list)) {
            StringJoiner joiner = new StringJoiner(delimiter, prefix, postfix);
            list.forEach(item -> joiner.add(String.valueOf(item)));
            return joiner.toString();
        }

        return prefix + postfix;
    }

    public static <T extends Enum> List<String> toEnumListOfStrings(List<T> list) {
        if (ListUtils.notEmpty(list)) {
            return list.stream().map(Enum::name).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static <T> List<List<T>> chopIntoParts(final List<T> ls, final int iParts) {
        final List<List<T>> lsParts = new ArrayList<List<T>>();
        final int iChunkSize = ls.size() / iParts;
        int iLeftOver = ls.size() % iParts;
        int iTake = iChunkSize;

        for (int i = 0, iT = ls.size(); i < iT; i += iTake) {
            if (iLeftOver > 0) {
                iLeftOver--;

                iTake = iChunkSize + 1;
            } else {
                iTake = iChunkSize;
            }

            lsParts.add(new ArrayList<T>(ls.subList(i, Math.min(iT, i + iTake))));
        }

        return lsParts;
    }

    public static <T> List<List<T>> partitionList(final List<T> input, final int partitionSize){

        final List<List<T>> result = new LinkedList<>();

        if(ListUtils.notEmpty(input)){
            int size = input.size();
            BatchIterator batchIterator = new BatchIterator(0, size, partitionSize);

            int lastIndex = batchIterator.next();
            while (batchIterator.hasNext()){

                int currentIndex = batchIterator.next();

                result.add(input.subList(lastIndex, currentIndex));

                lastIndex = currentIndex;
            }
        }

        return result;
    }

    public static <T> boolean isSorted(Iterable<T> iterable, Comparator<T> c) {
        Iterator<T> iter = iterable.iterator();
        if (!iter.hasNext()) {
            return true;
        }
        T t = iter.next();
        while (iter.hasNext()) {
            T t2 = iter.next();
            if (c.compare(t,t2) > 0) {
                return false;
            }
            t = t2;
        }
        return true;
    }

    public static <T> List<T> asArrayList(T[]... array){
        List<T> list = new ArrayList<T>(array.length);

        for(T[] item: array){
            if(item!=null){
                Collections.addAll(list, item);
            }
        }

        return list;
    }

}

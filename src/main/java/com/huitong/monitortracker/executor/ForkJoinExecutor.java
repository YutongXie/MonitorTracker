package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class ForkJoinExecutor extends RecursiveTask<List<NumberUpperLimitBreachResult>> {
    private static final int threshold = 2;
    private int start;
    private int end;
    private List<List<NumberUpperLimitBreachMetaData>> fullList;

    public ForkJoinExecutor(int start, int end, List<List<NumberUpperLimitBreachMetaData>> fullList) {
        this.start = start;
        this.end = end;
        this.fullList = fullList;
    }

    @Override
    protected List<NumberUpperLimitBreachResult> compute() {
        System.out.println("Compute - process id-" + Thread.currentThread().getId());
        List<NumberUpperLimitBreachResult> resultList = new ArrayList<>();
        boolean canCompute = (end -start) <= threshold;
        if(canCompute) {
            for(int i = start; i < end ; i ++) {
                List<NumberUpperLimitBreachMetaData> value = fullList.get(i);

//                String sql = generateCurrentValueSQL(value);
//                String currentValue = businessProcessorDAO.getTableColumnCurrentValue(sql);
                String currentValue ="ACCOUNT_ID-12345;CLASS_ID-45678";
                String[] columnCurrentValueArray = currentValue.split(";");
                for (String columnCurrentValue : columnCurrentValueArray) {
                    NumberUpperLimitBreachResult result = new NumberUpperLimitBreachResult();
                    result.setSchemaName(value.get(0).getSchemaName());
                    result.setTableName(value.get(0).getTableName());
                    String columnName = columnCurrentValue.substring(0, columnCurrentValue.indexOf("-"));
                    String columnValue = columnCurrentValue.substring(columnCurrentValue.indexOf("-") + 1);
                    result.setColumnName(columnName);
//                    result.setCurrentValue(convertToDecimal(columnValue));
//                    result.setLimitValue(getLimitValue(value, columnName));
                    result.setActive("A");
                    resultList.add(result);
                }
            }
        } else {
            int middle = (start + end)/2;
            ForkJoinExecutor leftExcutor = new ForkJoinExecutor(start, middle, fullList);
            ForkJoinExecutor rightExcutor = new ForkJoinExecutor(middle, end, fullList);
//            leftExcutor.fork();
//            rightExcutor.fork();
            invokeAll(leftExcutor, rightExcutor);
            List<NumberUpperLimitBreachResult> leftResult = leftExcutor.join();
            List<NumberUpperLimitBreachResult> rightResult = rightExcutor.join();
            resultList.addAll(leftResult);
            resultList.addAll(rightResult);
        }
        return resultList;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

        List<List<NumberUpperLimitBreachMetaData>> fullList = new ArrayList<>();
        for(int i =0; i< 6; i ++) {
            List<NumberUpperLimitBreachMetaData> metaDataList = new ArrayList<>();
            fullList.add(metaDataList);

            for(int j = 0 ; j < 3; j ++) {
                NumberUpperLimitBreachMetaData metaData = new NumberUpperLimitBreachMetaData();
                metaData.setTableName("TABLE-" + i +"-" +j);
                metaData.setSchemaName("SCHEMA-" + i +"-" + j);
                metaData.setColumnName("COLUMN-" + i +"-" + j);
                metaDataList.add(metaData);
            }
        }

        ForkJoinExecutor executor = new ForkJoinExecutor(0, 6, fullList);
        Future<List<NumberUpperLimitBreachResult>> result = pool.submit(executor);
        try {
            List<NumberUpperLimitBreachResult> re = result.get();
            System.out.println( result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

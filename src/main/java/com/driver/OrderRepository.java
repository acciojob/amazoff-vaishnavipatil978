package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String,Order> orderHashMap;
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap;
    HashMap<String ,String> orderDeliveryDb;
    HashMap<String, List<String>> deliveryPartnerOrderListDb;

    public OrderRepository(){
        orderHashMap = new HashMap<>();
        deliveryPartnerHashMap = new HashMap<>();
        orderDeliveryDb = new HashMap<>();
        deliveryPartnerOrderListDb = new HashMap<>();
    }
    boolean addOrder(Order order){
        if(orderHashMap.containsKey(order.getId())) return false;

        orderHashMap.put(order.getId(), order); return true;
    }
    boolean addPartner(String partner){
        if(deliveryPartnerHashMap.containsKey(partner)) return false;

        DeliveryPartner obj = new DeliveryPartner(partner);
        deliveryPartnerHashMap.put(partner,obj); return true;
    }
    boolean addOrderPartnerPair(String order,String partner){
        if(orderHashMap.containsKey(order) && deliveryPartnerHashMap.containsKey(partner)){
            orderDeliveryDb.put(order,partner);

            List<String> orderList= new ArrayList<>();
            if(deliveryPartnerOrderListDb.containsKey(partner)) orderList=deliveryPartnerOrderListDb.get(partner);
            orderList.add(order);
            deliveryPartnerOrderListDb.put(partner,orderList);
            deliveryPartnerHashMap.get(partner).increaseOrders();

            return true;
        }
        return false;
    }

    Order getOrderById(String orderId){
        try {
            return orderHashMap.get(orderId);
        }
        catch (Exception e){
            return null;
        }
    }
    DeliveryPartner getPartnerById(String partnerId){
        try {
            return deliveryPartnerHashMap.get(partnerId);
        }
        catch (Exception e){
            return null;
        }
    }

    Integer getOrderCountByPartnerId(String partnerId){
        try {
            return deliveryPartnerHashMap.get(partnerId).getNumberOfOrders();
        }
        catch (Exception e){
            return 0;
        }
    }

    List<String> getOrdersByPartnerId(String partnerId){
        return deliveryPartnerOrderListDb.get(partnerId);
    }

    List<String> getAllOrders(){
        return new ArrayList<>(orderHashMap.keySet());
    }

    Integer getCountOfUnassignedOrders(){
        int count = 0;

        for(String orderId : orderHashMap.keySet()){
            if(!orderDeliveryDb.containsKey(orderId)) count++;
        }

        return count;
    }

    Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        try {
            int giventime = (Integer.parseInt(time.substring(0,2))*60)+Integer.parseInt(time.substring(3,5));
            int count=0;

            for(String order : deliveryPartnerOrderListDb.get(partnerId)){
                if(orderHashMap.get(order).getDeliveryTime()>giventime) count++;
            }

            return count;
        }
        catch (Exception e){
            return 0;
        }
    }

    String getLastDeliveryTimeByPartnerId(String partnerId){
        if(!deliveryPartnerOrderListDb.containsKey(partnerId)) return "00:00";

        int lastDeliveryTime = 0;

        for(String order : deliveryPartnerOrderListDb.get(partnerId)){
            lastDeliveryTime = Math.max(lastDeliveryTime,orderHashMap.get(order).getDeliveryTime());
        }

        String hours = String.valueOf(lastDeliveryTime/60);
        String minutes = String.valueOf(lastDeliveryTime%60);

        if(hours.length()==1) hours="0"+hours;
        if(minutes.length()==1) minutes="0"+minutes;

        return hours+":"+minutes;
    }

    void deletePartnerById(String partnerId){
        deliveryPartnerHashMap.remove(partnerId);

        for(String orderId : deliveryPartnerOrderListDb.get(partnerId)){
            orderDeliveryDb.remove(orderId);
        }

        deliveryPartnerOrderListDb.remove(partnerId);
    }

    void deleteOrderById(String orderId){
        try {
            orderHashMap.remove(orderId);
            String deliveryPartnerId = orderDeliveryDb.get(orderId);
            deliveryPartnerOrderListDb.get(deliveryPartnerId).remove(orderId);
            deliveryPartnerHashMap.get(deliveryPartnerId).decreaseOrders();
        }
        catch (Exception e){

        }
    }
}

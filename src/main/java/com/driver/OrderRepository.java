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
    void addOrder(Order order){
        if(orderHashMap.containsKey(order.getId())) return ;

        orderHashMap.put(order.getId(), order);
    }
    void addPartner(String partner){
        if(deliveryPartnerHashMap.containsKey(partner)) return;

        DeliveryPartner obj = new DeliveryPartner(partner);
        deliveryPartnerHashMap.put(partner,obj);
    }
    void addOrderPartnerPair(String order,String partner){

        if(orderDeliveryDb.containsKey(order)) {
            return;
        }

        orderDeliveryDb.put(order,partner);

        List<String> orderList= new ArrayList<>();
        if(deliveryPartnerOrderListDb.containsKey(partner)) orderList=deliveryPartnerOrderListDb.get(partner);
        orderList.add(order);
        deliveryPartnerOrderListDb.put(partner,orderList);
        deliveryPartnerHashMap.get(partner).setNumberOfOrders(orderList.size());

    }

    Order getOrderById(String orderId){

        return orderHashMap.get(orderId);
    }


    DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerHashMap.get(partnerId);
    }

    Integer getOrderCountByPartnerId(String partnerId){
        return deliveryPartnerOrderListDb.get(partnerId).size();
    }

    List<String> getOrdersByPartnerId(String partnerId){
        return deliveryPartnerOrderListDb.get(partnerId);
    }

    List<String> getAllOrders(){
        List<String> orders= new ArrayList<>(orderHashMap.keySet());
        return orders;
    }

    Integer getCountOfUnassignedOrders(){
        int count = 0;

        for(String orderId : orderHashMap.keySet()){
            if(!orderDeliveryDb.containsKey(orderId)) count++;
        }

        return count;
    }

    Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        int giventime = Integer.valueOf(time.substring(3))+Integer.valueOf(time.substring(0,2))*60;
        int count=0;

        for(String order : deliveryPartnerOrderListDb.get(partnerId)){
            if(orderHashMap.get(order).getDeliveryTime()>giventime) count++;
        }

        return count;
    }

    String getLastDeliveryTimeByPartnerId(String partnerId){
        List<String> ordersList = deliveryPartnerOrderListDb.get(partnerId);
        Collections.sort(ordersList, (a,b)->orderHashMap.get(a).getDeliveryTime()-orderHashMap.get(b).getDeliveryTime());
        String orderId = ordersList.get(ordersList.size()-1);
        int time = orderHashMap.get(orderId).getDeliveryTime();
        StringBuilder lastTime = new StringBuilder();
        int hours = time/60;
        int minutes = time%60;
        if(hours<10) {
            lastTime.append(0);
        }
        lastTime.append(hours);
        lastTime.append(':');
        if(minutes<10) {
            lastTime.append(0);
        }
        lastTime.append(minutes);
        return lastTime.toString();
    }

    void deletePartnerById(String partnerId){
        deliveryPartnerHashMap.remove(partnerId);

        if(deliveryPartnerOrderListDb.containsKey(partnerId)){

            for(String orderId : deliveryPartnerOrderListDb.get(partnerId)){
                orderDeliveryDb.remove(orderId);
            }

            deliveryPartnerOrderListDb.remove(partnerId);

        }
    }

    void deleteOrderById(String orderId){

        if(orderDeliveryDb.containsKey(orderId)) {
            String partnerId = orderDeliveryDb.get(orderId);
            List<String> ordersList = deliveryPartnerOrderListDb.get(partnerId);
            ordersList.remove(orderId);
            deliveryPartnerOrderListDb.put(partnerId, ordersList);
            orderDeliveryDb.remove(orderId);
            deliveryPartnerHashMap.get(partnerId).setNumberOfOrders(ordersList.size());

        }
        orderHashMap.remove(orderId);
    }
}

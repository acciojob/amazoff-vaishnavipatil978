package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    boolean addOrder(Order order){
        return orderRepository.addOrder(order);
    }
    boolean addPartner(String partner){
        return orderRepository.addPartner(partner);
    }
    boolean addOrderPartnerPair(String order,String partner){return orderRepository.addOrderPartnerPair(order,partner);}
    Order getOrderById(String orderId){ return orderRepository.getOrderById(orderId); }

    DeliveryPartner getPartnerById(String partnerId){ return orderRepository.getPartnerById(partnerId); }

    Integer getOrderCountByPartnerId(String partnerId){ return orderRepository.getOrderCountByPartnerId(partnerId); }

    List<String> getOrdersByPartnerId(String partnerId){  return orderRepository.getOrdersByPartnerId(partnerId); }

    List<String> getAllOrders(){ return orderRepository.getAllOrders(); }

    Integer getCountOfUnassignedOrders(){ return orderRepository.getCountOfUnassignedOrders();}

    Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(time,partnerId);
    }

    String getLastDeliveryTimeByPartnerId(String partnerId){ return orderRepository.getLastDeliveryTimeByPartnerId(partnerId); }

    void deletePartnerById(String partnerId){ orderRepository.deletePartnerById(partnerId);}

    void deleteOrderById(String orderId){ orderRepository.deleteOrderById(orderId); }

}

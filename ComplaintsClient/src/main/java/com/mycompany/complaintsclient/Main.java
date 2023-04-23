/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.complaintsclient;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 *
 * @author kamil
 */
public class Main {

    public static void main(String[] args) {
        String URI = "http://localhost:8080/Complaints/resources/complaints";

        Client client = ClientBuilder.newClient();

        List<Complaint> allComplaints = client
                .target(URI)
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Complaint>>() {});
        
        allComplaints.forEach(System.out::println);

        Long openComplaintId = allComplaints
                .stream()
                .filter(c -> "open".equals(c.getStatus()))
                .findAny()
                .get()
                .getId();
        
        Complaint complaint = client
                .target(URI)
                .path(String.valueOf(openComplaintId))
                .request(MediaType.APPLICATION_JSON)
                .get(Complaint.class);
        
        System.out.println(complaint.toString());
        
        complaint.setStatus("closed");
        
        client
                .target(URI)
                .path(String.valueOf(complaint.getId()))
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(complaint, MediaType.APPLICATION_JSON));
        
        List<Complaint> openComplaints = client
                .target(URI)
                .queryParam("status", "open")
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<Complaint>>() {});
        
        openComplaints.forEach(System.out::println);

        client.close();
    }
}

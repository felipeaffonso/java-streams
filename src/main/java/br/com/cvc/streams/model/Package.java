package br.com.cvc.streams.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Package {
    private String hotel;
    private String flight;
    private Integer price;
    private List<SonPackage> sonPackages;

    public Package(String hotel, String flight, Integer price) {
        this.hotel = hotel;
        this.flight = flight;
        this.price = price;
    }

    public String getIdentifier() {
        return this.hotel + "|" + this.flight;
    }
}
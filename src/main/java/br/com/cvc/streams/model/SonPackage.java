package br.com.cvc.streams.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SonPackage {
    private String hotel;
    private String voo;
    private Integer price;
}
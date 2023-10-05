package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.ProductoDeseado;
import com.serendipity.ecommerce.dto.ProductoDeseadoDTO;

import static org.springframework.beans.BeanUtils.copyProperties;

public class ProductoDeseadoDTOMapper {
    public static ProductoDeseadoDTO fromFavoritoProducto(ProductoDeseado productoDeseado) {
        ProductoDeseadoDTO productoDeseadoDTO = new ProductoDeseadoDTO();
        copyProperties(productoDeseado, productoDeseadoDTO);
        productoDeseadoDTO.setProducto(ProductoDTOMapper.fromProducto(productoDeseado.getProducto()));
        return productoDeseadoDTO;
    }
}

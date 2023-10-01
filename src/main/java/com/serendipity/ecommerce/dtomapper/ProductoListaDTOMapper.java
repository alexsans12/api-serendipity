package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.ProductoLista;
import com.serendipity.ecommerce.dto.ProductoListaDTO;

import static org.springframework.beans.BeanUtils.copyProperties;

public class ProductoListaDTOMapper {
    public static ProductoListaDTO fromProductoLista(ProductoLista productoLista) {
        ProductoListaDTO productoListaDTO = new ProductoListaDTO();
        copyProperties(productoLista, productoListaDTO);
        productoListaDTO.setProducto(ProductoDTOMapper.fromProducto(productoLista.getProducto()));
        return productoListaDTO;
    }
}

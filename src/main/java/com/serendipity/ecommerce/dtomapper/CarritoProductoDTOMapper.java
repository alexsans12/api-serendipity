package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.CarritoProducto;
import com.serendipity.ecommerce.dto.CarritoProductoDTO;

import static org.springframework.beans.BeanUtils.copyProperties;

public class CarritoProductoDTOMapper {
    public static CarritoProductoDTO fromCarritoProducto(CarritoProducto carritoProducto) {
        CarritoProductoDTO carritoProductoDTO = new CarritoProductoDTO();
        copyProperties(carritoProducto, carritoProductoDTO);
        carritoProductoDTO.setProducto(ProductoDTOMapper.fromProducto(carritoProducto.getProducto()));
        return carritoProductoDTO;
    }
}

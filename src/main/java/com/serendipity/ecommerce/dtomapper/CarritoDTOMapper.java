package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Carrito;
import com.serendipity.ecommerce.domain.CarritoProducto;
import com.serendipity.ecommerce.dto.CarritoDTO;
import com.serendipity.ecommerce.dto.CarritoProductoDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

public class CarritoDTOMapper {

    public static CarritoDTO fromCarrito(Carrito carrito, List<CarritoProducto> carritoProductos) {
        CarritoDTO carritoDTO = new CarritoDTO();
        copyProperties(carrito, carritoDTO);
        List<CarritoProductoDTO> carritoProductoDTOList = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal descuento = BigDecimal.ZERO;
        for (CarritoProducto carritoProducto : carritoProductos) {
            BigDecimal subtotal = getSubtotal(carritoProducto);

            // Accumulate the subtotal in the total
            total = total.add(subtotal);
            descuento = descuento.add(getDescuento(carritoProducto));
            carritoProductoDTOList.add(CarritoProductoDTOMapper.fromCarritoProducto(carritoProducto));
        }
        carritoDTO.setTotal(total);
        carritoDTO.setDescuento(descuento);
        carritoDTO.setCarritoProductos(carritoProductoDTOList);
        return carritoDTO;
    }

    private static BigDecimal getSubtotal(CarritoProducto carritoProducto) {
        BigDecimal precioProducto = carritoProducto.getProducto().getPrecio();
        BigDecimal cantidad = BigDecimal.valueOf(carritoProducto.getCantidad());

        // Calculate the subtotal without discount
        return precioProducto.multiply(cantidad);
    }

    private static BigDecimal getDescuento(CarritoProducto carritoProducto) {
        BigDecimal precioProducto = carritoProducto.getProducto().getPrecio();
        BigDecimal cantidad = BigDecimal.valueOf(carritoProducto.getCantidad());

        BigDecimal descuento = carritoProducto.getProducto().getDescuento();

        descuento = descuento.multiply(precioProducto);

        // Calculate the subtotal without discount
        return descuento.multiply(cantidad);
    }
}

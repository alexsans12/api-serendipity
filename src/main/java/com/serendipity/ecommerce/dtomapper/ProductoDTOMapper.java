package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Producto;
import com.serendipity.ecommerce.dto.ProductoDTO;
import com.serendipity.ecommerce.dto.UsuarioDTO;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.fromUsuario;
import static org.springframework.beans.BeanUtils.copyProperties;

public class ProductoDTOMapper {
    public static ProductoDTO fromProducto(Producto producto) {
        ProductoDTO productoDTO = new ProductoDTO();
        copyProperties(producto, productoDTO);
        productoDTO.setNombreCreadoPor(producto.getCreadoPorUsuario().getNombre());
        productoDTO.setApellidoCreadoPor(producto.getCreadoPorUsuario().getApellido());
        productoDTO.setNombreCategoria(producto.getCategoria().getNombre());

        if(producto.getModificadoPorUsuario() != null) {
            UsuarioDTO usuarioDTO = fromUsuario(producto.getModificadoPorUsuario());
            productoDTO.setNombreModificadoPor(usuarioDTO.getNombre());
            productoDTO.setApellidoModificadoPor(usuarioDTO.getApellido());
        }

        if(producto.getCategoria().getCategoriaPadre() != null) {
            productoDTO.setNombreCategoriaPadre(producto.getCategoria().getCategoriaPadre().getNombre());
        }

        if(producto.getImagenesProducto() != null && !producto.getImagenesProducto().isEmpty()) {
            productoDTO.setImagenes(producto.getImagenesProducto());
        }

        return productoDTO;
    }
}

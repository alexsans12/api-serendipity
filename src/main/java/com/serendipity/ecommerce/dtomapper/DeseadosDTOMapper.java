package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Deseados;
import com.serendipity.ecommerce.domain.ProductoDeseado;
import com.serendipity.ecommerce.dto.DeseadosDTO;
import com.serendipity.ecommerce.dto.ProductoDeseadoDTO;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

public class DeseadosDTOMapper {
    public static DeseadosDTO fromDeseados(Deseados deseados, List<ProductoDeseado> productosDeseados) {
        DeseadosDTO deseadosDTO = new DeseadosDTO();
        copyProperties(deseados, deseadosDTO);
        List<ProductoDeseadoDTO> productoDeseadoDTOList = new ArrayList<>();

        for (ProductoDeseado productoDeseado : productosDeseados) {
            productoDeseadoDTOList.add(ProductoDeseadoDTOMapper.fromFavoritoProducto(productoDeseado));
        }
        deseadosDTO.setProductosDeseados(productoDeseadoDTOList);
        return deseadosDTO;
    }
}

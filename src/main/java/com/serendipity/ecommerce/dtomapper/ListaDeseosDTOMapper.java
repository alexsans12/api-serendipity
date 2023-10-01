package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.ListaDeseos;
import com.serendipity.ecommerce.domain.ProductoLista;
import com.serendipity.ecommerce.dto.ListaDeseosDTO;
import com.serendipity.ecommerce.dto.ProductoListaDTO;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

public class ListaDeseosDTOMapper {
    public static ListaDeseosDTO fromListaDeseos(ListaDeseos listaDeseos, List<ProductoLista> productoListaList) {
        ListaDeseosDTO listaDeseosDTO = new ListaDeseosDTO();
        copyProperties(listaDeseos, listaDeseosDTO);
        List<ProductoListaDTO> productoListaDTOList = new ArrayList<>();

        for (ProductoLista productoLista : productoListaList) {
            productoListaDTOList.add(ProductoListaDTOMapper.fromProductoLista(productoLista));
        }
        listaDeseosDTO.setProductoListaDTOList(productoListaDTOList);
        return listaDeseosDTO;
    }
}

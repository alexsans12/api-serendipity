package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Marca;
import com.serendipity.ecommerce.dto.MarcaDTO;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import org.springframework.beans.BeanUtils;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.fromUsuario;

public class MarcaDTOMapper {
    public static MarcaDTO fromMarca(Marca marca) {
        MarcaDTO marcaDTO = new MarcaDTO();
        BeanUtils.copyProperties(marca, marcaDTO);
        UsuarioDTO usuarioDTO = fromUsuario(marca.getCreadoPorUsuario());
        marcaDTO.setNombreCreadoPor(usuarioDTO.getNombre());
        marcaDTO.setApellidoCreadoPor(usuarioDTO.getApellido());

        if(marca.getModificadoPorUsuario() != null) {
            usuarioDTO = fromUsuario(marca.getModificadoPorUsuario());
            marcaDTO.setNombreModificadoPor(usuarioDTO.getNombre());
            marcaDTO.setApellidoModificadoPor(usuarioDTO.getApellido());
        }

        return marcaDTO;
    }

    public static Marca toMarca(MarcaDTO marcaDTO) {
        Marca marca = new Marca();
        BeanUtils.copyProperties(marcaDTO, marca);
        return marca;
    }
}

package com.serendipity.ecommerce.dtomapper;

import com.serendipity.ecommerce.domain.Categoria;
import com.serendipity.ecommerce.dto.CategoriaDTO;
import com.serendipity.ecommerce.dto.UsuarioDTO;
import org.springframework.beans.BeanUtils;

import static com.serendipity.ecommerce.dtomapper.UsuarioDTOMapper.fromUsuario;
import static org.springframework.beans.BeanUtils.copyProperties;

public class CategoriaDTOMapper {
    public static CategoriaDTO fromCategoria(Categoria categoria) {
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        copyProperties(categoria, categoriaDTO);
        UsuarioDTO usuarioDTO = fromUsuario(categoria.getCreadoPorUsuario());
        categoriaDTO.setNombreCreadoPor(usuarioDTO.getNombre());
        categoriaDTO.setApellidoCreadoPor(usuarioDTO.getApellido());

        if(categoria.getModificadoPorUsuario() != null) {
            usuarioDTO = fromUsuario(categoria.getModificadoPorUsuario());
            categoriaDTO.setNombreModificadoPor(usuarioDTO.getNombre());
            categoriaDTO.setApellidoModificadoPor(usuarioDTO.getApellido());
        }

        if(categoria.getCategoriaPadre() != null) {
            categoriaDTO.setCategoriaPadre(fromCategoria(categoria.getCategoriaPadre()));
        }

        return categoriaDTO;
    }

    /*public static Categoria toCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        copyProperties(categoriaDTO, categoria);
        copyProperties(categoriaDTO.getCategoriaPadre(), categoria.getCategoriaPadre());
        return categoria;
    }*/
}

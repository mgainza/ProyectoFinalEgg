package com.turistearg.Servicios;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.turistearg.Entidades.Foto;
import com.turistearg.Entidades.Publicacion;
import com.turistearg.Entidades.Usuario;
import com.turistearg.Excepciones.ErrorServicio;
//import com.turistearg.Repositorios.CategoriasRepositorio;
import com.turistearg.Repositorios.PublicacionRepositorio;
import com.turistearg.Repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicacionServicio {
	@Autowired
	private FotoServicio serviciosFoto;
	
	@Autowired
	private PublicacionRepositorio publicacionRepositorio;
	
//	@Autowired
//	private CategoriasRepositorio categoriaRepositorio;
	
	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Transactional
	public void crear(MultipartFile archivo, String idUsuario, String descripcion, String idCategoria) throws ErrorServicio {
		
		validar(idUsuario, idCategoria);

		Publicacion publicacion = new Publicacion();
		
		Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
		
		if (respuesta.isPresent()) {
		
			Usuario user = usuarioRepositorio.findById(idUsuario).get();
			publicacion.setUsuario(user);
		}
		
		publicacion.setDescripcion(descripcion);

		Foto foto = serviciosFoto.guardar(archivo);
		
		publicacion.setFoto(foto);

		publicacionRepositorio.save(publicacion);
	}
	
	
	private void validar(String nombreDeUsuario, String categoria)
			throws ErrorServicio {

		if (nombreDeUsuario == null || nombreDeUsuario.isEmpty()) {
			throw new ErrorServicio("El nombre no puede ser nulo.");
		}

		if (categoria == null || categoria.isEmpty()) {
			throw new ErrorServicio("La categoria no puede ser nula.");
		}
	}
        
        
        public List<Publicacion> buscarPublicacionesPorCategoria(String idCategoria) throws ErrorServicio{
            
            if (idCategoria == null) {
                throw new ErrorServicio("La id no puede ser nula");
            }
            
            List<Publicacion> publicaciones = new ArrayList();
            publicaciones = publicacionRepositorio.buscarPublicacionesPorCategoria(idCategoria);
            
            
            if (publicaciones == null) {
                throw new ErrorServicio("No se encontraron las publicaciones para la categoria especificada");
            }
            
            return publicaciones;
            
        }
        
        public List<Publicacion> buscarPublicacionesPorUsuario(String idUsuario)throws ErrorServicio{
            
            List<Publicacion> publicaciones = new ArrayList();
            publicaciones = publicacionRepositorio.buscarPublicacionesPorUsuario(idUsuario);
            
            if (publicaciones == null) {
                throw new ErrorServicio("No se encontraron las publicaciones para el usuario especificado"); 
            }
            return publicaciones;
        
        }
}

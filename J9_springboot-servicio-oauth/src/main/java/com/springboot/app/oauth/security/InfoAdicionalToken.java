package com.springboot.app.oauth.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.services.IUsuarioService;

//CLASE97: CLASE PARA AGREGAR INFORMACIÓN ADICIONAL AL TOKEN
	//1.-EN UN MAP SE GUARDA LA INFORMACIÓN ADICIONAL DEL TOKEN. <String,Object> : SE PONE OBJECT YA QUE EL VALOR PODRÍA SER DE CUALQUIER TIPO.
	//2.-EL "INFO" SE ADICIONA AL ACCESSTOKEN. EL accessToken ES DE UNA INTERFAZ MUY GENERICA, POR LO TANTO HAY QUE USAR OTRA MÁS ESPECIFICA. SE HACE UNA CAST CON LA CLASE QUE SIS SIRVE.

@Component
public class InfoAdicionalToken implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		//1
		Map<String,Object>info = new HashMap<String,Object>();
		
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		info.put("nombre", usuario.getNombre());
		info.put("apellido", usuario.getApellido());
		info.put("email", usuario.getEmail());
		//2
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		return accessToken;
	}
	
	
	@Autowired
	private IUsuarioService usuarioService;
}

package br.ufba.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

//Recebe a aplicação zipada e coloca no barramento
@Path("/deployApp")
public class Manager {

	public Manager() {
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response recebeApp(
			@FormDataParam("App") FormDataContentDisposition detalhesApp,
			@FormDataParam("App") InputStream appZipada) {
		String content;
		String path = "R:\\Desenvolvimento\\WorkspaceJavaEE32\\"
				+ "Arquivos de teste\\retorno\\" + detalhesApp.getFileName();
		int sucesso = saveToFile(appZipada, path);
		if (sucesso > 0) {
			content = "App received";
			return Response.status(200).entity(content).build();
		} else {
			content = "Error !";
			return Response.status(500).entity(content).build();
		}

	}

	// save uploaded file to new location
	private int saveToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = null;
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}

	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String testeGet() {
		return "<html> " + "<title>" + "Manager" + "</title>" + "<body><h3>"
				+ "Faz deploy da app" + "</body></h3>" + "</html> ";
	}
}

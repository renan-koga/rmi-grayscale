package rmi.grayscale;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Grayscale {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedImage img = null;
	    File f = null;
	    String path;
	    
	    //lendo imagem
	    Scanner scan = new Scanner(System.in);
	    
	    System.out.println("#######################################################################");
	    System.out.println("########### Conversão de imagem colorida para tons de cinza ###########");
	    System.out.println("#######################################################################");
	    
	    System.out.println("\nDigite o diretório da imagem (ex: /home/username/image.jpg):");
	    path = scan.nextLine();
	    
//	    path = "/home/renan/Downloads/teste.jpg";
	    try {
	      f = new File(path);
	      img = ImageIO.read(f);
	    } catch(IOException e) {
	      System.out.println(e);
	    }

	    //pegando width e height da imagem
	    int width = img.getWidth();
	    int height = img.getHeight();

	    //convertendo para tons de cinza
	    for(int y = 0; y < height; y++){
	      for(int x = 0; x < width; x++){
	        int p = img.getRGB(x,y);

	        int a = (p>>24)&0xff;
	        int r = (p>>16)&0xff;
	        int g = (p>>8)&0xff;
	        int b = p&0xff;

	        //calculando média
	        int avg = (r+g+b)/3;

	        //trocando o valor RGB pela média calculada
	        p = (a<<24) | (avg<<16) | (avg<<8) | avg;

	        img.setRGB(x, y, p);
	      }
	    }

	    System.out.println("\nDigite o diretório de saída da imagem convertida (ex: /home/username/image-out.jpg):");
	    path = scan.nextLine();
	    
//	    path = "/home/renan/Downloads/teste-out.jpg";
	    
	    //escrevendo nova imagem (tons de cinza)
	    try {
	      f = new File(path);
	      ImageIO.write(img, "jpg", f);
	    } catch(IOException e) {
	      System.out.println(e);
	    }
	    
	    scan.close();
	    
	    System.out.println("\n\nImagem convertida com sucesso!");
	}

}

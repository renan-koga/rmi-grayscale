import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GrayscaleTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage img = null;
	    File f = null;
	    String path;
	    
	    //lendo imagem
	    Scanner scan = new Scanner(System.in);
	    
	    System.out.println("#######################################################################");
	    System.out.println("########### Conversão de imagem colorida para tons de cinza ###########");
	    System.out.println("#######################################################################");
	    
//	    System.out.println("\nDigite o diretório da imagem (ex: /home/username/image.jpg):");
//	    path = scan.nextLine();
	    
	    path = "/home/renan/Downloads/corinthians.jpg";
	    try {
	      f = new File(path);
	      img = ImageIO.read(f);
	    } catch(IOException e) {
	      System.out.println(e);
	    }

	    //pegando width e height da imagem
	    int width = img.getWidth();
	    int height = img.getHeight();
	    
//	    int y = height/2;
	    int id = 1;
	    int i = 0;
	    BufferedImage[] splitImages = new BufferedImage[3];
	    BufferedImage imgAux;
	    for (int y = 0; y < img.getHeight(); y += height/3) {
	    	imgAux = img.getSubimage(0, y, width, height/3);
	    	splitImages[i] = imgAux;
	    	ImageIO.write(imgAux, "jpg", new File("/home/renan/corinthians-" + id++ + ".jpg"));
	    	i++;
	    }
	    
	    int chunkWidth, chunkHeight;
	    int type;
	    type = splitImages[0].getType();
	    chunkWidth = splitImages[0].getWidth();
	    chunkHeight= splitImages[0].getHeight();
	    
	    
	    BufferedImage finalImg = new BufferedImage(chunkWidth, chunkHeight*3, type);
	    
	    for (i=0; i<3; i++) {
	    	finalImg.createGraphics().drawImage(splitImages[i], 0, chunkHeight*i, null);
	    }
	    
	    ImageIO.write(finalImg, "jpeg", new File("/home/renan/corinthians-99.jpg"));
	    
	    //convertendo para tons de cinza
//	    for(int y = 0; y < height; y++){
//	      for(int x = 0; x < width; x++){
//	        int p = img.getRGB(x,y);
//
//	        int a = (p>>24)&0xff;
//	        int r = (p>>16)&0xff;
//	        int g = (p>>8)&0xff;
//	        int b = p&0xff;
//
//	        //calculando média
//	        int avg = (r+g+b)/3;
//
//	        //trocando o valor RGB pela média calculada
//	        p = (a<<24) | (avg<<16) | (avg<<8) | avg;
//
//	        img.setRGB(x, y, p);
//	      }
//	    }

//	    System.out.println("\nDigite o diretório de saída da imagem convertida (ex: /home/username/image-out.jpg):");
//	    path = scan.nextLine();
	    
	    path = "/home/renan/corinthians.jpg";
	    
	    //escrevendo nova imagem (tons de cinza)
//	    try {
//	      f = new File(path);
//	      ImageIO.write(img, "jpg", f);
//	    } catch(IOException e) {
//	      System.out.println(e);
//	    }
	    
	    scan.close();
	    
	    System.out.println("\n\nImagem convertida com sucesso!");
	}

}

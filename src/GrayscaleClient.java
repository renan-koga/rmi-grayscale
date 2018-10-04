import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GrayscaleClient {
	public static void main(String[] args) throws IOException {
		LinkedList<String> serversName = new LinkedList<String>();
        BufferedImage img = null;
	    File f = null;
	    
	    int serverNum, width, height, type, splitSize;
	    String pathIn, pathOut;
        
        Scanner scan = new Scanner(System.in);
        
	    System.out.println("#######################################################################");
	    System.out.println("########### Conversão de imagem colorida para tons de cinza ###########");
	    System.out.println("#######################################################################");
	    
	    System.out.println("\nDigite o diretório da imagem (ex: /home/username/image.jpg):");
	    pathIn = scan.nextLine();
	    System.out.println("\nDigite o diretório de saída da imagem (ex: /home/username/image-out.jpg):");
	    pathOut = scan.nextLine();
	    System.out.println("\nDigite a quantidade de servidores necessários para a conversão:");
	    serverNum = Integer.parseInt(scan.nextLine());
	    
//	    pathIn = "/home/renan/Downloads/corinthians.jpg";
//	    pathOut = "/home/renan/corinthians-99.jpg";
//	    serverNum = 4;

	    /**
	     * Lendo os n servidores.
	     */
	    String serverName;
	    System.out.println();
	    for(int i=0; i<serverNum; i++) {
	    	System.out.println("Digite o nome do servidor " + (i+1));
	    	serverName = scan.nextLine();
	    	
	    	serversName.add(serverName);
	    }
	    
	    /**
	     * Lendo imagem através do diretório recebido.
	     */
	    try {
	    	f = new File(pathIn);
	    	img = ImageIO.read(f);
	    } catch(IOException e) {
	    	System.out.println("\nNão foi possível abrir a imagem " + pathIn);
	    	System.out.println("Encerrando programa...");
	    	System.out.println(e);
	    	System.exit(-1);
	    }
	    
	    long startTime = System.currentTimeMillis();
	    
	    /**
	     * Pegando tipo, width e height da imagem.
	     */
	    type = img.getType();
	    width = img.getWidth();
	    height = img.getHeight();
	    
	    splitSize = height/serverNum;
	    
	    /**
	     * Separando a imagem em n partes iguais, de acordo com o número de servidores a serem utilizados.
	     */
	    LinkedList<int[]> splitImagesBytes;	    
	    splitImagesBytes = splitImage(img, splitSize);
	    
	    /**
	     * Inicializando as interfaces.
	     */
	    GrayscaleInterface grayscales[] = new GrayscaleInterface[serverNum];
	    try {
	    	System.out.println();
	    	for(int i=0; i<serverNum; i++) {
	    		serverName = serversName.get(i);
	    		grayscales[i] = (GrayscaleInterface) Naming.lookup(serverName);
	    		System.out.println("Lookup of server "+ serverName + " done.");
	    	}
	    } catch (Exception e) {
	    	System.out.println("\nCaught an exception doing name lookup on server: " + e);
            System.exit(-1);
	    }
	    
	    /**
	     * Convertendo partes da imagem em diferentes threads.
	     */
	    try {
		    Thread[] t = new Thread[serverNum];
		    for(int i=0; i<serverNum; i++) {
		    	t[i] = convertImage(grayscales[i], splitImagesBytes, i);
		    	t[i].start();
		    }
		    
		    /**
		     * Esperando que todas as thread terminem de executar antes de continuar com a main.
		     */
		    for(int i=0; i<serverNum; i++) {
				t[i].join();
			}
	    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	    
	    /**
	     * Juntando todas as partes da imagem convertida.
	     */
	    BufferedImage imageOut;
	    imageOut = joinImages(splitImagesBytes, width, height, type, splitSize);
	    ImageIO.write(imageOut, "jpg", new File(pathOut));
	    
	    long finishTime = System.currentTimeMillis();
	    
	    /**
	     * Calculando o tempo gasto para a conversão.
	     */
	    SimpleDateFormat date = new SimpleDateFormat("mm:ss SSSSS");
	    System.out.println("\n\n\nTempo gasto para a conversão (mm:ss:ms): " + date.format(new java.util.Date(finishTime - startTime)));
	    
	    System.out.println("\nImagem convertida com sucesso!");

	    scan.close();
    }
	
	public static LinkedList<int[]> splitImage(BufferedImage image, int splitSize) throws IOException {
		LinkedList<int[]> splitImagesBytes = new LinkedList<int[]>();
		int width, height, widthAux, heightAux, index;
		
		/**
	     * pegando width e height da imagem
	     */
	    width = image.getWidth();
	    height = image.getHeight();
		
	    index = 0;
		BufferedImage img;
		for (int y = 0; y < height; y += splitSize) {
			int[] imageByte = new int[width*splitSize];
			img = image.getSubimage(0, y, width, splitSize);
			
			widthAux = img.getWidth();
			heightAux = img.getHeight();
			
			img.getRGB(0, 0, widthAux, heightAux, imageByte, 0, widthAux);
			
			splitImagesBytes.add(index, imageByte);
			index++;
			
			ImageIO.write(img, "jpg", new File("/home/renan/corinthians-" + index + ".jpg"));
		}
		
		return splitImagesBytes;		
	}
	
	public static BufferedImage joinImages(LinkedList<int[]> splitImagesBytes, int width, int height, int type, int splitSize) {
		BufferedImage imageOut = new BufferedImage(width, height, type);
		int[] imagePixels = splitImagesBytes.get(0);
		int index, k;
		
		k = 0;
		index = -1;
        for (int i = 0; i < height; i++){
        	if (i%splitSize == 0) {
        		k = 0;
        		index++;
        		imagePixels = splitImagesBytes.get(index);
        	}
        	for (int j = 0; j < width; j++){
        		imageOut.setRGB(j, i, imagePixels[k]);
                k++;
            }
        }
		
		return imageOut;
	}
	
	public static Thread convertImage(GrayscaleInterface grayscale, LinkedList<int[]> splitImagesBytes, int index) {
		Thread t = new Thread() {
			int[] convertedImagePixels;
			
			@Override
			public void run() {
				try {
					convertedImagePixels = grayscale.convert(splitImagesBytes.get(index));
					splitImagesBytes.remove(index);
					splitImagesBytes.add(index, convertedImagePixels);
				} catch (RemoteException e) {
					System.out.println("Exception caught while getting grayscale conversion in Thread: "+ e);
			       	e.printStackTrace();
		        	System.exit(-1);
				}
			}
			
		};
		
		return t;		
	}

}

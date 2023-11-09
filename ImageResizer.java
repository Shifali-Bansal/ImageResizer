import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageResizer {
    public static void main(String[] args) {
        // Replace "path/to/your/input/image.jpg" with the actual path of your input image
        String inputImagePath = "path/to/your/input/image.jpg";

        // Replace "path/to/your/output/image_compressed.jpg" with the desired output path
        String outputImagePath = "path/to/your/output/image_compressed.jpg";

        compressImage(inputImagePath);

        System.out.println("Image compression completed. Check the output at: " + outputImagePath);
    }

    public static void compressImage(String filepath) {
        try {
            // Load the input image
            File inputImageFile = new File(filepath);
            BufferedImage originalImage = ImageIO.read(inputImageFile);

            // Set the desired width and height
            int targetWidth = 600;
            int targetHeight = 400;

            // Create a resized image
            BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);

            // Compress and save the resized image with a target file size (80KB in this case)
            saveImageWithTargetSize(resizedImage, filepath, 80 * 1024);

            System.out.println("Image compressed successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Resize the image to the specified dimensions
    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        Image resultingImage = originalImage.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = resizedImage.getGraphics();
        g.drawImage(resultingImage, 0, 0, null);
        g.dispose();
        return resizedImage;
    }

    private static void saveImageWithTargetSize(BufferedImage image, String outputPath, int targetSize) throws IOException {
        float quality = 0.9f; // Initialize the quality variable

        // Compress the image until it's within the target file size
        while (true) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);

            if (os.toByteArray().length <= targetSize) {
                // Save the compressed image to the output file
                try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                    fos.write(os.toByteArray());
                }
                break; // Exit the loop if the target size is met
            }

            if (quality <= 0) {
                throw new IOException("Unable to compress image within target size.");
            }

            quality -= 0.1; // Decrease the quality
        }
    }
}

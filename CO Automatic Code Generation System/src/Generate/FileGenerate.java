package Generate;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileGenerate
{
    List<String> ans;

    public FileGenerate(List<String>ans)
    {
        this.ans=ans;
    }

    public void update()
    {
        String fileName = "output.txt";
        try
        {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(fileName)));
            for (String text : this.ans)
            {
                bw.write(text);
                bw.newLine();
            }

            bw.close();
        } catch (IOException e)
        {
            System.out.println("–¥»Î ß∞‹¡À°£°£°£");
        }
    }
}

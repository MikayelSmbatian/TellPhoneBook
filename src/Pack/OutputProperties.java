package Pack;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OutputProperties extends Properties
{
    OutputProperties()
    {
        super();
        FileInputStream fis;
        try {
            fis = new FileInputStream("OutPutPROPERTIES.txt");
            this.load(fis);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

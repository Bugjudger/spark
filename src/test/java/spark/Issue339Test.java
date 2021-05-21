package spark;

import org.junit.Before;
import org.junit.Test;
import spark.util.SparkTestUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static spark.Spark.get;

public class Issue339Test {
    private static SparkTestUtil http = new SparkTestUtil(4567);

    private static final String ROUTE1 = "/greet/aaa";

    private static final String ROUTE2 = "/bbb";

    @Before
    public void setup() {
        get("/greet/:name",
            (req,res) -> new ModelAndView(req.params(), "greet"), new TemplateEngine() {
                @Override
                public String render(ModelAndView modelAndView) {
                    return modelAndView.getModel() + " from " + modelAndView.getViewName();
                }
            });

        get("/:test",
            (req,res) -> new ModelAndView(req.params(), "greet"), new TemplateEngine() {
                @Override
                public String render(ModelAndView modelAndView) {
                    return modelAndView.getModel() + " from root";
                }
            });
    }

    // CS304 Issue link: https://github.com/perwendel/spark/issues/339
    @Test
    public void testUrl1() throws Exception{
        try {
            SparkTestUtil.UrlResponse response = http.get(ROUTE1);
            assertEquals(200, response.status);
            assertEquals("{name=aaa} from greet", response.body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CS304 Issue link: https://github.com/perwendel/spark/issues/339
    @Test
    public void testUrl2() throws Exception{
        try {
            SparkTestUtil.UrlResponse response = http.get(ROUTE2);
            assertEquals(200, response.status);
            assertEquals("{test=bbb} from root", response.body);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

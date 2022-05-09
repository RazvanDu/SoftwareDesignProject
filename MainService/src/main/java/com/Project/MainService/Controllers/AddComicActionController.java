package com.Project.MainService.Controllers;

import com.Project.MainService.Utilities.ServiceType;
import com.Project.MainService.Utilities.UserType;
import com.Project.MainService.Utilities.Utils;
import com.sun.istack.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Add comic action controller.
 */
@RestController
public class AddComicActionController {

    /**
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Addcomic action.
     *
     * @param file        the file
     * @param previewLink the preview link
     * @param writerName  the writer name
     * @param price       the price
     * @param issueNumber the issue number
     * @param serie       the serie
     * @param title       the title
     * @param request     the request
     * @param response    the response
     * @throws IOException the io exception
     */
    @RequestMapping("/addcomicAction")
    public void addcomicAction(@RequestParam(name = "cover", required = false) MultipartFile file,
                               @RequestParam(name = "preview", required = false) String previewLink,
                               @RequestParam(name = "writer", required = false) String writerName,
                               @RequestParam(name = "price", required = false) String price,
                               @RequestParam(name = "issue", required = false) String issueNumber,
                               @RequestParam(name = "sel1", required = false) String serie,
                               @RequestParam(name = "title", required = false) String title,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {

        if (Utils.getUserType(restTemplate, request) != UserType.SELLER) {
            logger.warning("Add comic was accessed by an user of type " + Utils.getUserType(restTemplate, request));
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(file.getBytes(), false)));

        Map<String, String> mapped = new HashMap<>();
        mapped.put("cover", sb.toString());
        mapped.put("writer", writerName);
        mapped.put("price", price);
        mapped.put("issueNumber", issueNumber);
        mapped.put("sel1", serie);
        mapped.put("title", title);

        logger.info("Sent a request to add the comic " + title + "!");

        restTemplate.postForEntity(Utils.getDataURL(ServiceType.CATALOG_SERVICE, "/issues/add/issue"), mapped, Void.class);
        response.sendRedirect("/");

    }

}

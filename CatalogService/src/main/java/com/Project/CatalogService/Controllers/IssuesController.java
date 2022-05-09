package com.Project.CatalogService.Controllers;

import com.Project.CatalogService.Database.Writer;
import com.Project.CatalogService.Database.*;
import com.Project.CatalogService.Utilities.ResourceNotFoundException;
import com.google.gson.Gson;
import com.sun.istack.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * The type Issues controller.
 */
@RestController
@RequestMapping(path="/issues")
public class IssuesController {

    @Autowired
    private IssuesRepository issuesRepository;

    @Autowired
    private SeriesRepository seriesRepository;

    @Autowired
    private WritersRepository writersRepository;

    private List<Issue> catalog = new ArrayList<>();

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Gets all issues.
     *
     * @return the all issues
     */
    @GetMapping(path = "/get")
    //@Timed(value = "getIssues.time", description = "Time taken to find all the comics")
    public @ResponseBody
    String getAllIssues() {

        if (catalog.size() < 1)
            catalog = issuesRepository.findAll();

        return new Gson().toJson(catalog);

    }

    /**
     * Resize image buffered image.
     *
     * @param originalImage the original image
     * @param targetWidth   the target width
     * @param targetHeight  the target height
     * @return the buffered image
     */
    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {

        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);

        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        return outputImage;

    }

    /**
     * Img to base 64 string string.
     *
     * @param img        the img
     * @param formatName the format name
     * @return the string
     */
    public String imgToBase64String(final RenderedImage img, final String formatName) {

        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try (final OutputStream b64os = Base64.getEncoder().wrap(os)) {
            ImageIO.write(img, formatName, b64os);
        } catch (final IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        return os.toString();

    }

    /**
     * Gets issues by id.
     *
     * @param issueId the issue id
     * @return the issues by id
     * @throws ResourceNotFoundException the resource not found exception
     * @throws IOException               the io exception
     */
    @GetMapping("/{id}")
    public @ResponseBody
    String getIssuesById(@PathVariable(value = "id") Integer issueId) throws ResourceNotFoundException, IOException {

        for (Issue issue : catalog){
            if (issue.getId() == issueId)
                return new Gson().toJson(ResponseEntity.ok().body(issue));
        }

        Issue issue = issuesRepository.findById(issueId).orElseThrow(() ->
                new ResourceNotFoundException("Issues not found with id :" + issueId));

        issue.setHasLink(issue.getLinkPreview() != null && !issue.getLinkPreview().equals(""));

        String base64Image = issue.getCover().split(",")[1];
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage result = resizeImage(img, 200, 200);

        String resultStr = "data:image/jpeg;base64," + imgToBase64String(result, "jpeg");

        issue.setCover(resultStr);

        issuesRepository.save(issue);

        return new Gson().toJson(ResponseEntity.ok().body(issue));

    }

    /**
     * Gets issues by writer.
     *
     * @param writerId the writer id
     * @return the issues by writer
     */
    @GetMapping("findByWriter/{id}")
    public @ResponseBody
    String getIssuesByWriter(@PathVariable(value = "id") Integer writerId) {

        List<Issue> issues = issuesRepository.findByWriter(writerId);

        return new Gson().toJson(ResponseEntity.ok().body(issues));

    }

    /**
     * Gets issues by series.
     *
     * @param refactorId the refactor id
     * @return the issues by series
     */
    @GetMapping("findBySeries/{id}")
    public @ResponseBody
    String getIssuesBySeries(@PathVariable(value = "id") Integer refactorId) {

        List<Issue> issues = issuesRepository.findBySeries(refactorId);

        return new Gson().toJson(ResponseEntity.ok().body(issues));

    }

    /**
     * Add issue.
     *
     * @param data the data
     */
    @PostMapping("add/issue")
    public void addIssue(@RequestBody Map<String, String> data) {

        try {

            Series series = getSeriesObjectByName(data.get("sel1"));
            Writer writer = getWritersObjectByName(data.get("writer"));

            Issue issue = new Issue(series,
                                    data.get("cover"),
                                    writer,
                                    data.get("previewLink"),
                                    Integer.parseInt(data.get("issueNumber")),
                                    data.get("title"),
                                    Float.parseFloat(data.get("price")),
                                    0);

            logger.info("The issue " + issue.getTitle() + " has just been added!");

            issuesRepository.save(issue);
            catalog = issuesRepository.findAll();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Gets series object by name.
     *
     * @param seriesName the series name
     * @return the series object by name
     */
    public Series getSeriesObjectByName(String seriesName) {

        List<Series> series = seriesRepository.findByTitle(seriesName);

        if (series.size() != 0)
            return series.get(0);

        return null;
    }

    /**
     * Gets writers object by name.
     *
     * @param writerName the writer name
     * @return the writers object by name
     */
    public Writer getWritersObjectByName(String writerName) {

        List<Writer> writers = writersRepository.findByName(writerName);

        if (writers.size() != 0)
            return writers.get(0);

        return null;

    }

    /**
     * Delete issue string.
     *
     * @param issueId the issue id
     * @return the string
     */
    @GetMapping("delete/{id}")
    public @ResponseBody String deleteIssue(@PathVariable(value = "id") Integer issueId) {

        Issue issueToDelete = issuesRepository.findById(issueId).get();
        issuesRepository.delete(issueToDelete);

        catalog = issuesRepository.findAll();

        logger.info("The issue " + issueToDelete.getTitle() + " has just been deleted!");

        return "Success";

    }

    /**
     * Sellcomic string.
     *
     * @param issueId the issue id
     * @param amount  the amount
     * @return the string
     */
    @GetMapping("sellcomic/{id}/{amount}")
    public @ResponseBody String sellcomic(@PathVariable(value = "id") Integer issueId,
                                          @PathVariable(value = "amount") Integer amount) {

        Issue issueToSell = issuesRepository.findById(issueId).get();
        issueToSell.setSold(issueToSell.getSold() + amount);

        issuesRepository.save(issueToSell);

        logger.info("The issue " + issueToSell.getTitle() + " has just been sold!");

        return "Success";

    }

}
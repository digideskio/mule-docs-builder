import org.junit.Test;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.File;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class RootNodeInHtmlTocTests {

    @Test
    public void fromAsciiDocTocPage_ReturnsNewObject() {
        RootNodeInHtmlToc htmlToc = RootNodeInHtmlToc.fromTocAsciiDocPage(getValidPage());
        assertThat(htmlToc, instanceOf(RootNodeInHtmlToc.class));
    }

    @Test
    public void fromAsciiDocTocPage_RootNodeIsNotNull() {
        RootNodeInHtmlToc htmlToc = RootNodeInHtmlToc.fromTocAsciiDocPage(getValidPage());
        assertTrue(htmlToc.getRootNode() != null);
    }

    @Test
    public void getRootNodeFromRawTocHtml_FromValidTocDoc_HasProperStructure() {
        RootNodeInHtmlToc htmlToc = RootNodeInHtmlToc.fromTocAsciiDocPage(getValidPage());
        TocNode result = htmlToc.getRootNode();
        assertTrue(result.getTitle().contentEquals("CloudHub"));
        assertTrue(result.getChildren().size() == 12);
    }

    @Test(expected = DocBuildException.class)
    public void getRootNodeFromRawTocHtml_FromInvalidTocDoc_ThrowsException() {
        RootNodeInHtmlToc htmlToc = RootNodeInHtmlToc.fromTocAsciiDocPage(getInvalidPage());
    }

    @Test(expected = DocBuildException.class)
    public void getRootNodeFromRawTocHtml_FromTocDocWithMoreThanOneRootNode_ThrowsException() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "bad-files", "toc.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        RootNodeInHtmlToc htmlToc = RootNodeInHtmlToc.fromTocAsciiDocPage(page);
    }

    private AsciiDocPage getValidPage() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "toc.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        return page;
    }

    private AsciiDocPage getInvalidPage() {
        String path = Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"});
        AsciiDocPage page = AsciiDocPage.fromFile(new File(path));
        return page;
    }
}
package com.E3N.admin.catalogo.domain.category;

import com.E3N.admin.catalogo.domain.UnitTest;
import com.E3N.admin.catalogo.domain.category.Category;
import com.E3N.admin.catalogo.domain.exceptions.DomainException;
import com.E3N.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullNamme_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final var expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError(){
        final var expectedName = "Fi ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveOK() {
        final var expectedName = "Filmes";
        final var expectedDescription = "  ";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAnInvalidDescription_whenCallNewCategoryAndValidate_thenShouldThrowDomainException() {
        final var expectedName = "Filmes";
        final var expectedDescription = """
                 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris gravida, leo vel faucibus egestas, enim sem gravida tortor, non vulputate nunc diam et leo. Sed eu nisl tortor. Fusce hendrerit nec turpis sed condimentum. Donec aliquet ornare enim, eu tincidunt mauris accumsan a. Phasellus feugiat viverra tellus nec feugiat. Maecenas in egestas velit, non semper dui. Aliquam erat volutpat. Phasellus porta, metus ut commodo placerat, mauris ex posuere orci, ut viverra enim velit in mauris.
                                
                 Vivamus vitae lorem arcu. Ut tempor mauris sit amet tellus viverra, mollis luctus quam sollicitudin. Integer eu nibh gravida, imperdiet odio quis, convallis dui. Ut at purus molestie, dictum metus non, iaculis turpis. Nullam varius augue sed metus vestibulum, blandit blandit nisl egestas. Integer nec maximus arcu. Suspendisse vitae mattis ipsum, eget pharetra justo. Proin hendrerit dictum ornare. Vivamus facilisis sem eu lorem pulvinar vestibulum. Nulla iaculis tellus quis eleifend cursus. Ut accumsan ligula tristique leo pharetra, a pharetra ex auctor. Suspendisse at tristique orci, eget semper sem.
                                
                 Vivamus tempus rhoncus rutrum. Phasellus id rutrum elit. Donec in nulla lorem. Vivamus id eleifend quam. Sed fermentum quam a molestie maximus. Integer maximus rutrum metus, sit amet placerat orci tempus eu. Vivamus vel rhoncus mi. Mauris vulputate feugiat blandit. Sed consectetur dui in hendrerit rutrum. Cras condimentum luctus ligula vel ultricies.
                                
                 Integer eleifend ipsum sed vestibulum pretium. Suspendisse elementum neque tellus, eget imperdiet dui imperdiet ut. Nam tempor tortor nec eros vestibulum, vitae dignissim lectus pharetra. Donec mollis faucibus mi, sit amet mollis ipsum. Morbi rutrum, metus non eleifend cursus, lorem mi euismod neque, quis venenatis nunc nunc at mauris. Nullam nec suscipit ligula, eu venenatis sapien. In vehicula rhoncus sollicitudin. Mauris ut ligula at sapien tempus vestibulum. Ut rutrum, est vitae laoreet lobortis, justo nulla egestas elit, at rhoncus metus magna a lectus.
                                
                 Pellentesque scelerisque mi metus, eget sollicitudin nibh fringilla in. Ut enim lorem, convallis ut tortor vel, vulputate maximus tortor. Maecenas nec urna pulvinar, vulputate tellus id, iaculis dolor. Phasellus sed erat vitae lacus convallis varius vel iaculis nunc. Nullam sed mollis erat, vel elementum felis. Aliquam erat volutpat. Donec diam velit, convallis sed molestie sit amet, interdum vitae nisi.
                                
                 Donec vel sagittis nibh. Sed tristique nibh eu ante feugiat, a pharetra tellus lobortis. Vestibulum felis lectus, luctus in orci sit amet, elementum molestie enim. Etiam viverra mollis arcu a euismod. Maecenas nec sem nec turpis rutrum sollicitudin a eget lectus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nullam at pharetra orci. Donec eu justo at ipsum sollicitudin hendrerit. Nulla dictum congue augue, quis interdum sapien porttitor at. Ut consequat mauris id elementum dictum. Praesent et arcu sagittis, semper tortor sed, blandit enim. In convallis aliquet leo ac condimentum. Proin hendrerit finibus porttitor. Ut aliquet tellus neque, ac convallis lorem convallis eget.
                                
                 Vivamus molestie volutpat augue sed fermentum. Cras aliquet turpis at velit aliquam ullamcorper. Maecenas efficitur quis velit nec efficitur. Proin dictum purus ut pulvinar volutpat. Nulla aliquam mi a ligula tempor, non vestibulum lorem fringilla. Sed id massa sed est suscipit viverra. Proin quis molestie turpis, eget ultrices augue. Nam arcu mauris, facilisis et commodo non, viverra sit amet ipsum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nulla nec purus nibh. Duis eu volutpat ante, ut semper elit. Donec laoreet commodo tempus. Donec malesuada elit ac sapien tristique accumsan ut at sem.
                                
                 Etiam aliquet sapien vitae sagittis vulputate. Sed non pulvinar lacus, eget volutpat dui. In nec eros risus. Mauris luctus sapien dolor, vitae mattis sapien finibus non. Vestibulum dictum justo vel sagittis eleifend. Nullam tortor nunc, dapibus vitae porta non, pharetra feugiat erat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vestibulum nulla sem, bibendum lacinia sem ac, mollis maximus elit. Maecenas vitae dolor dictum purus lobortis convallis. Nunc auctor, metus at finibus cursus, arcu ipsum commodo lacus, eget tristique erat lectus ut diam. Donec bibendum nisl vitae tellus viverra pharetra.
                                
                 Nullam magna lectus, euismod eu molestie at, vulputate auctor velit. Vivamus leo justo, consequat eget risus vel, venenatis facilisis neque. Quisque rutrum nunc in nisi suscipit, at ultrices augue ultrices. Nunc sed ante ac est vehicula ullamcorper eu at arcu. Curabitur arcu nibh, sollicitudin non finibus quis, laoreet non ex. Nam viverra viverra risus, at volutpat magna venenatis mollis. Vestibulum congue posuere mauris quis pulvinar. Mauris convallis bibendum nisl quis elementum. Duis elementum fermentum metus, vitae commodo lectus mattis non. Pellentesque aliquet sodales condimentum. Quisque lobortis diam cursus risus facilisis dapibus. Integer tincidunt sem non risus fermentum, sed finibus massa rhoncus. Ut ornare accumsan leo, et semper libero aliquam eu.
                                
                 Quisque hendrerit eros nec lorem iaculis, sed tristique tellus posuere. Vestibulum varius viverra nunc. In placerat libero vel gravida congue. Nam vehicula, mi vitae finibus rhoncus, dui arcu tincidunt tellus, sit amet feugiat dolor mauris id lorem. Proin enim sapien, mollis at pharetra varius, aliquet vitae nibh. Ut sit amet fringilla tellus. Vestibulum luctus in diam non congue. Nunc ornare ultricies fermentum.
                                
                 Donec nisi velit, facilisis vitae ante non, porta vulputate nunc. Proin egestas feugiat mauris, in fringilla felis pretium at. Nullam ut nisl in augue dictum eleifend in vel tellus. Morbi dignissim massa risus, non consequat arcu ultrices nec. Vivamus accumsan aliquet nibh vel elementum. Nam placerat ligula quam, sed viverra odio tincidunt non. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In velit turpis, elementum vitae ultrices tincidunt, dictum id dui.
                                
                 Curabitur eu ex at leo consequat sagittis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus aliquet, velit semper lacinia ornare, est quam viverra metus, at pellentesque justo elit sit amet erat. Integer nec justo rutrum, suscipit sapien a, euismod enim. Nunc vitae enim nec diam efficitur dignissim. Phasellus convallis odio sit amet dolor efficitur, sed pellentesque mauris pellentesque. Donec nec enim quis tortor vulputate laoreet eu in tortor.
                                
                 Suspendisse malesuada, tellus non laoreet convallis, nisi quam ultrices elit, ac faucibus eros nisi at nunc. In vulputate consequat fermentum. Praesent quis augue rutrum, efficitur augue sit amet, commodo risus. In lorem ex, tempor id condimentum sed, mattis sed velit. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce vitae neque justo. Etiam mattis enim ut libero semper porttitor nec nec odio. Mauris porta nunc mi. Mauris sollicitudin fringilla ullamcorper. Quisque a elit et neque convallis auctor. Fusce porttitor risus nec turpis mollis, eu maximus ante pellentesque. Etiam a elit in ligula tempor auctor sit amet non erat. In tristique quam eu libero euismod varius. Mauris vulputate facilisis nulla, vitae suscipit mauris consectetur quis. Etiam feugiat rhoncus tempor.
                                
                 Duis a erat a turpis pharetra suscipit. Proin sollicitudin cursus elit, in consectetur velit pretium id. Vivamus in urna tincidunt, congue justo a, suscipit quam. Nunc auctor tincidunt rutrum. Vestibulum consectetur condimentum justo, vestibulum egestas ex efficitur ut. Duis felis tellus, auctor id velit at, efficitur posuere ex. Vestibulum ut nisi porttitor, fermentum nunc in, tristique massa. Aliquam ac dolor ligula. Pellentesque efficitur commodo orci, tristique vulputate neque lobortis non. Curabitur sollicitudin ut dui at dapibus. Integer fermentum velit sit amet dui tempus, nec elementum metus feugiat. Maecenas mattis ultricies sapien, vitae vulputate ante molestie in. Suspendisse libero nulla, pellentesque placerat metus sed, mattis finibus odio. Etiam ante lacus, porta quis dui a, malesuada egestas ligula. Donec tincidunt orci ac libero placerat, eget tempor ipsum interdum.
                                
                 Mauris vulputate nulla eget ligula ornare, quis interdum leo vehicula. Cras venenatis, arcu in hendrerit dapibus, ex orci vulputate ante, ac rutrum ante eros vitae metus. Praesent imperdiet neque sapien, quis lobortis nulla lobortis placerat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Nulla quis lacus vitae ligula fermentum blandit ac sit amet ante. Duis auctor odio risus, et condimentum urna blandit venenatis. Nullam vehicula sem sit amet nibh iaculis convallis. Nulla vehicula eros nibh, quis scelerisque nisi porttitor sit amet. Phasellus ornare augue ex, id tincidunt eros laoreet ac.
                                
                 Maecenas ac laoreet tortor. Morbi in tempor turpis, a molestie sem. Quisque non dolor vitae leo commodo mollis eget in sem. Pellentesque laoreet sit amet velit in euismod. Nullam et malesuada mauris. Curabitur scelerisque est non lorem eleifend aliquam. Morbi scelerisque massa at lectus accumsan scelerisque et sollicitudin sem.
                                
                 Praesent tristique sem eu tortor tristique ultricies. Sed lorem est, varius eu bibendum a, mollis luctus ligula. Mauris nec nisi nec metus convallis bibendum vel ac ipsum. Cras at nibh in arcu fermentum laoreet. Curabitur ultrices eleifend nunc. Nulla nisl ex, eleifend a suscipit in, eleifend quis nisl. Praesent in ante vitae velit aliquam luctus. Phasellus fringilla bibendum felis. Fusce velit est, venenatis non semper sit amet, pellentesque ultrices mi. In tempus arcu sapien, eget condimentum dolor scelerisque sit amet. Fusce porta dolor eu blandit dictum. Donec scelerisque vulputate libero in pellentesque. Aenean gravida tempor elit consectetur venenatis. Fusce venenatis libero at rhoncus mollis.
                                
                 Aenean quis commodo turpis, ut blandit arcu. Aliquam risus nulla, sagittis ac odio vitae, mollis venenatis nisl. Nunc nec aliquet sapien. Curabitur fermentum felis eget dolor ullamcorper, vel consequat mi sodales. Nulla nunc velit, placerat eu nibh eget, tempus tempor purus. Etiam viverra odio vel eros imperdiet tempor. Sed tincidunt tempor iaculis. Aenean non dolor ac nisi cursus fringilla. Proin placerat suscipit sem. Vivamus quis odio at est egestas vulputate vitae a lorem.
                                
                 Curabitur et lacus gravida erat dictum dapibus. Integer euismod pharetra diam, et placerat massa elementum id. Praesent at mauris ultrices, rutrum ex non, laoreet arcu. Vestibulum velit nulla, tristique a nulla eget, sollicitudin venenatis neque. Vivamus tincidunt orci non mollis interdum. Suspendisse potenti. In sit amet sapien ut orci mollis porttitor et sit amet nulla. Vestibulum sed auctor eros. Nunc vehicula tincidunt lacus ac viverra. Aliquam erat volutpat. Sed consectetur a odio et ullamcorper. Nullam vestibulum volutpat convallis. Nunc fermentum dolor nec purus bibendum cursus. Donec commodo suscipit orci, a ornare tellus vehicula eget. Sed ut quam ut tortor mattis elementum at quis nisi.
                                
                 Phasellus tincidunt, nisi quis egestas suscipit, turpis lacus suscipit nibh, ut semper arcu mauris a augue. Nullam placerat ligula justo, ut egestas nisi euismod ut. Vestibulum interdum iaculis elit. Sed blandit faucibus massa eu rhoncus. In feugiat elit metus, eu lacinia nisi pulvinar eu. Morbi justo velit, congue et tincidunt id, suscipit et libero. Nulla malesuada lacus eget nibh posuere, ac interdum tellus molestie. Integer et est gravida, tristique leo et, pretium felis. Donec consequat viverra rutrum. Quisque volutpat erat id lacus imperdiet sodales. Duis eleifend laoreet varius.
                                
                 Curabitur suscipit, est eget sagittis auctor, quam purus euismod diam, eu ornare dolor massa ornare mi. Quisque pretium, nisl at tempus aliquet, elit libero maximus tortor, quis bibendum eros libero sit amet ex. Maecenas eleifend sem nisl, sit amet lacinia sem convallis nec. Vivamus feugiat risus quis libero tempor, eget posuere nibh iaculis. Ut dictum diam vel accumsan mollis. Ut iaculis magna ut viverra commodo. Donec augue libero, venenatis sit amet mi sagittis, lacinia facilisis lacus. Integer nisl elit, pharetra et ipsum eget, cursus feugiat sem. Vivamus blandit ornare dapibus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Maecenas pellentesque risus nec porta tincidunt. Aliquam erat volutpat. Pellentesque a libero fermentum tellus scelerisque blandit et sit amet justo. Proin sagittis metus in nulla dictum, eget accumsan diam vulputate. Mauris pulvinar turpis vitae lectus interdum luctus. Proin mollis blandit eros, eu fermentum nulla sodales quis.
                                
                 Cras at pellentesque ante. Ut at ante iaculis, sodales massa vehicula, finibus enim. Curabitur volutpat, purus interdum facilisis interdum, dui elit venenatis justo, non posuere ante felis nec tortor. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla elementum ligula vitae augue eleifend tincidunt. Praesent vel libero sed turpis cursus pharetra. Quisque ultrices nisl sit amet mi dictum consequat. Quisque quis fringilla mauris, sed sodales dui. Mauris cursus ex vel odio egestas, ac placerat est faucibus. Sed aliquam leo est, vel efficitur urna hendrerit vel. Sed posuere rhoncus nisl, sit amet pulvinar odio vulputate sed.
                                
                 Nulla facilisi. Morbi id lorem sit amet nisl aliquam convallis id cursus massa. Nullam consequat odio at luctus vehicula. Morbi eget semper dolor. Praesent eget lorem eget risus malesuada volutpat non id mauris. Quisque volutpat, mauris id ultrices elementum, tellus ex consequat dui, at mollis nisi dolor at nulla. Pellentesque interdum orci vitae mi efficitur dictum. In venenatis metus non dolor molestie, id finibus massa iaculis. Praesent blandit erat nulla, eu porta justo scelerisque eu. Vivamus laoreet magna non neque fringilla tempor. Sed eget nulla mattis, molestie turpis in, commodo nulla. Quisque orci odio, porttitor quis molestie ut, accumsan id turpis. Proin in nibh pretium, molestie lectus vel, accumsan libero. Ut in convallis eros, at dapibus purus. Donec elementum feugiat diam sit amet dignissim.
                                
                 Etiam dolor ligula, fermentum sed ex a, facilisis tristique justo. Nulla vitae vulputate nibh, sed elementum leo. Duis tempus bibendum imperdiet. Donec vestibulum semper risus. Duis sagittis ornare sollicitudin. Morbi feugiat lorem dictum eros rhoncus varius. Pellentesque porttitor lacus at consectetur eleifend. Sed dignissim ante felis, tincidunt viverra ipsum malesuada vitae.
                                
                 Vestibulum a volutpat dolor. Aenean et pretium justo, id lacinia diam. Phasellus convallis, risus a consequat consequat, nulla tortor semper lectus, nec placerat odio massa in nisl. Duis nulla justo, ullamcorper eget placerat ultricies, blandit vel ex. Vivamus posuere dolor eu ex blandit sagittis id vel tellus. Vestibulum sagittis, mi non fringilla faucibus, lectus justo ultrices nisi, sit amet accumsan arcu massa nec sem. Fusce ullamcorper massa sit amet purus feugiat lacinia. Pellentesque in neque vel nunc viverra posuere a vel lacus. Sed sollicitudin, urna ut dignissim semper, ex purus auctor nisl, a feugiat urna tortor vitae est. Donec et porttitor ante. Fusce commodo auctor nunc eu tincidunt. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam dapibus purus at nisl fringilla, in gravida augue auctor. Praesent cursus eu ante sit amet porta. In hendrerit mauris congue ante sollicitudin, vel convallis massa rutrum. In gravida venenatis enim vitae finibus.
                                
                 Aenean at quam a orci elementum cursus. Vivamus at ligula luctus, gravida arcu id, tempor leo. In sagittis non massa in consectetur. Aenean dictum pellentesque sapien ut porttitor. Nunc cursus non orci tincidunt venenatis. Proin vel interdum urna. Phasellus aliquam rhoncus ipsum. Nam aliquam efficitur consectetur. Pellentesque venenatis ex et pharetra dictum. Vestibulum porttitor vehicula scelerisque. Suspendisse sit amet pretium sapien, tincidunt tincidunt turpis. Proin elementum aliquet metus at vehicula. Nullam suscipit lectus quis felis vestibulum, nec laoreet nibh efficitur. Sed consequat dui eu sapien ullamcorper aliquam. Sed sed mauris id magna fermentum pellentesque.
                                
                 Curabitur porttitor neque in lorem dictum, et tincidunt magna gravida. Maecenas imperdiet tortor justo, eget laoreet purus ornare non. Ut non eleifend arcu. Mauris id augue at nibh gravida auctor et eget ipsum. Fusce diam purus, auctor eget dui a, volutpat congue mi. Duis non aliquet mauris, a luctus arcu. Cras nisl erat, ultrices id neque sed, volutpat tempus velit. Donec non urna imperdiet, tempus justo sed, laoreet enim. Phasellus enim justo, elementum nec aliquet ac, rutrum ut risus.
                                
                 Vestibulum varius sodales imperdiet. Aliquam interdum quis elit nec interdum. Nunc eu ligula vitae libero fermentum interdum. Aenean orci sem, porttitor vel vestibulum in, pharetra sed sapien. Nam quis ex ut odio dignissim varius non eget lorem. Integer eget nulla risus. Integer nec convallis tellus, non hendrerit sem. Maecenas ut faucibus neque. Aliquam tempus pharetra lacus et vehicula.
                                
                 Phasellus rutrum vehicula diam, ut pulvinar diam fermentum nec. Phasellus quis lobortis sem. Vivamus a velit suscipit, consequat sem egestas, suscipit augue. Ut at fringilla ligula. Sed luctus, purus ut scelerisque ultrices, mi erat rhoncus felis, sed pulvinar est est a erat. Aliquam sed condimentum sem, aliquam fermentum massa. Maecenas vel justo mollis, feugiat risus ut, finibus lorem. Fusce a mi elementum, mollis tellus quis, tempor nulla. Praesent laoreet blandit molestie. Phasellus a semper nunc. Nulla facilisi. Morbi non mattis orci. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                
                 Vestibulum consequat vestibulum mauris, eget faucibus purus lobortis nec. Nullam aliquet in lectus at malesuada. Mauris in velit vel eros placerat pulvinar id vel sapien. Sed consequat hendrerit libero eu fringilla. Etiam non commodo ligula. Mauris quis mauris eget nisi maximus finibus. Sed eu mi hendrerit urna malesuada congue. Sed nec tellus vel orci ullamcorper gravida. Aliquam elementum odio eros, eget bibendum nulla dignissim a. Duis elementum nisl vel semper accumsan. Nunc sagittis felis maximus tempor tempor. Nullam tortor lorem, ultrices vel consequat eu, consequat et mi. Integer tortor tellus, molestie at sollicitudin quis, finibus sed dolor. Ut varius odio arcu, consectetur porta sem efficitur nec. Duis luctus tellus in ante vehicula tincidunt. Vivamus vestibulum dolor eros, et facilisis massa finibus ac.
                                
                 Aenean eleifend, augue sed scelerisque volutpat, ipsum sapien pretium neque, in suscipit mauris metus sed est. Nulla in dignissim dolor. Mauris fermentum erat vel lectus porttitor blandit. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cras in magna imperdiet, ultricies purus sed, eleifend tellus. Proin hendrerit blandit ipsum ac suscipit. Donec in magna euismod, suscipit justo eu, bibendum nisl. Interdum et malesuada fames ac ante ipsum primis in faucibus. Mauris nec erat eget lectus lacinia dignissim. Quisque condimentum viverra tristique. Etiam lacinia, dui in scelerisque aliquam, erat nisi vulputate magna, sed volutpat erat nisi in tortor. Nullam sollicitudin, risus in viverra lacinia, purus lorem feugiat felis, consequat ultrices sapien quam et metus. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Suspendisse cursus nulla sit amet ultricies fringilla.
                                
                 Donec vel arcu elit. Sed tempus fringilla nibh non placerat. Fusce velit augue, pellentesque ut malesuada et, blandit a nisi. Phasellus in nibh ac ligula pellentesque blandit. Nullam malesuada dapibus libero, ac bibendum ante laoreet vel. Nam sed luctus nulla. Curabitur ut diam ante. Sed pellentesque neque fermentum, eleifend ligula et, condimentum nibh. Suspendisse elementum lectus at risus lobortis suscipit. Aenean quis nunc justo. Aenean erat neque, dignissim sit amet sollicitudin non, finibus eget mauris. Cras eget iaculis mauris. Sed vulputate arcu et metus commodo egestas. Quisque efficitur pharetra neque, at laoreet ipsum feugiat ut. Duis laoreet ut diam semper commodo. Quisque malesuada ac neque in consequat.
                                
                 Vestibulum suscipit porttitor pretium. Maecenas molestie nulla odio, ut vulputate felis luctus at. Etiam ac tortor libero. Pellentesque in quam turpis. Ut placerat, urna et mattis feugiat, justo enim tincidunt tellus, rutrum placerat augue mauris quis mi. Phasellus vitae ante dui. Nam gravida non ipsum at pulvinar. Nullam dui justo, pharetra nec diam in, fermentum congue sapien. Aenean at lectus volutpat velit rhoncus tempor eu eu sapien. Sed a blandit nibh. Quisque vulputate lacus a lectus ullamcorper imperdiet. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.
                                
                 Curabitur sagittis venenatis pretium. Suspendisse vel egestas nisl. Donec pulvinar feugiat porttitor. Phasellus pulvinar tellus ut pharetra luctus. Vestibulum maximus, ex eu dignissim mattis, nisi dui hendrerit turpis, quis rhoncus nulla mauris quis ipsum. Donec elit nisi, mattis ac blandit eget, imperdiet a nisi. Phasellus porta, dolor non tempus porta, nisi risus vulputate augue, id dictum ex risus non nisi. Nam maximus augue sed purus tempor maximus.
                                
                 Integer iaculis urna bibendum massa faucibus, ut sagittis erat imperdiet. Pellentesque id tortor felis. Nunc quis faucibus magna. Pellentesque at euismod eros. Mauris lobortis eu purus dictum accumsan. Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec eu mauris nulla.
                                
                 Integer egestas ullamcorper ex eu gravida. Fusce ut tincidunt felis. Donec blandit laoreet massa quis imperdiet. Morbi ex dui, maximus ac justo nec, tristique feugiat lorem. Proin at semper turpis. Aenean sit amet pulvinar tortor. In nunc lacus, tempus id feugiat nec, semper ac mi. Maecenas a pulvinar justo, vel maximus elit. Curabitur turpis lacus, volutpat vel fermentum non, commodo non ante. Fusce vel tortor pharetra, varius eros eu, consectetur dolor. Donec in leo quis tortor interdum ullamcorper. Praesent congue libero ut pellentesque lobortis.
                                
                 Phasellus massa nulla, tincidunt consectetur consectetur nec, gravida ac odio. Duis ac volutpat magna. Vivamus porttitor orci massa, ut cursus eros hendrerit sed. Aenean molestie ex sit amet ipsum iaculis vulputate. Cras condimentum mi eget faucibus ultricies. Maecenas tincidunt egestas libero id aliquet. Aliquam non justo ut massa gravida pharetra vitae ut odio. Quisque tempor orci quis pulvinar sollicitudin. Quisque maximus, diam ut efficitur varius, massa erat efficitur nibh, eu iaculis nulla mi nec justo.
                                
                 Nam porttitor ante id varius malesuada. Phasellus ac fringilla nunc. In hac habitasse platea dictumst. Donec mattis tempus ante, a porttitor dolor scelerisque at. Nullam sit amet lectus sapien. Ut pulvinar felis nec orci euismod molestie. Sed non est lorem. Vestibulum ut metus felis. Vivamus commodo elementum tortor vitae venenatis. Duis eget mauris luctus, iaculis lorem quis, efficitur lectus. Pellentesque sed metus ipsum. Morbi elementum convallis nulla sed consequat. Phasellus nec sapien ac diam pharetra lacinia non at metus. Integer semper sem tellus, tincidunt suscipit elit tempor non. Donec varius fermentum nunc et sollicitudin.
                                
                 Sed eleifend fermentum dolor, at sodales nisi aliquam a. In justo odio, venenatis convallis lobortis in, tincidunt et odio. Maecenas molestie justo ac scelerisque lacinia. Duis et mauris quis eros feugiat condimentum hendrerit ac purus. Curabitur euismod consectetur turpis et malesuada. Phasellus semper libero at lectus eleifend, id pretium ligula aliquam. Suspendisse ultricies sapien mauris, id rhoncus eros consequat eget. Pellentesque vitae aliquet orci, ut euismod nulla. In at sapien nulla. Praesent ut tellus at felis sollicitudin egestas. Fusce finibus sapien eget felis tristique, sed vehicula felis rhoncus. Nullam at nunc ex. Vivamus condimentum sem libero, non tristique urna faucibus sit amet. Suspendisse vestibulum erat eget blandit sollicitudin. Ut posuere urna in condimentum interdum. Mauris nec sapien non ex imperdiet sollicitudin.
                                
                 Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Cras turpis turpis, vehicula eget imperdiet ac, pulvinar ac libero. Donec semper sit amet magna sit amet ornare. Aliquam malesuada pulvinar laoreet. Morbi eu iaculis massa. Curabitur vel rhoncus justo, sit amet pulvinar augue. Nunc tempus placerat rhoncus. Phasellus vitae mattis orci, at tincidunt mauris. Proin non semper mauris, suscipit consequat ante. Pellentesque sodales ipsum volutpat ante dapibus, et aliquam elit vulputate. Interdum et malesuada fames ac ante ipsum primis in faucibus. In id quam tincidunt, interdum dolor nec, vehicula turpis.
                                
                 Suspendisse aliquet pellentesque augue, eget porttitor dui. Cras venenatis tortor sit amet sapien tincidunt, eget feugiat metus posuere. Suspendisse eget nisi ex. Pellentesque aliquet porttitor nibh sed finibus. Maecenas eget risus et ipsum dictum vehicula vitae eget eros. Praesent ultrices vel neque a iaculis. In vitae auctor dui. Phasellus efficitur, arcu ut bibendum rutrum, ligula nunc tempor ipsum, vel tempus tellus dolor et ligula. In sollicitudin eleifend interdum. Morbi vitae massa efficitur, imperdiet est a, aliquet arcu. Etiam at ornare turpis, hendrerit tempus neque.
                                
                 Suspendisse eget ligula quis eros venenatis porta vel eleifend ante. Curabitur non tortor a quam ornare vestibulum. Nunc nisi dolor, condimentum vel felis ac, faucibus posuere dui. Fusce vitae ante dapibus, luctus nisl vitae, faucibus quam. Etiam feugiat consequat diam vel blandit. Integer scelerisque libero et magna maximus pulvinar. Sed a eleifend purus. Nulla tristique vehicula tempor. Nullam viverra ipsum et nisi euismod porttitor. Integer eu felis laoreet, efficitur ligula molestie, consectetur est. Donec elementum nunc vitae eleifend finibus. Vivamus tempor sit amet tellus vel tempor.
                                
                 In at est viverra, tempor lorem id, luctus sem. Integer eget turpis vitae orci cursus euismod. Aenean ipsum tortor, rutrum sit amet magna ut, malesuada dapibus tellus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Fusce vel lorem vel dolor hendrerit vulputate non ut diam. Integer pellentesque nulla at vehicula congue. Proin at bibendum sapien. Nullam leo odio, luctus non odio sed, vestibulum congue magna. Sed ultricies pharetra purus, non ultricies massa dictum scelerisque.
                                
                 Duis dictum ultricies nisl, at mollis nunc molestie non. Quisque feugiat dictum est, at cursus massa finibus nec. Proin at diam id eros malesuada consequat in pulvinar elit. Phasellus pulvinar ultrices sem, ac euismod ante porttitor et. Vestibulum ut ultrices metus, sit amet ornare dui. Pellentesque elementum, nibh mollis vehicula euismod, erat arcu aliquam diam, ut convallis sapien erat a magna. Aliquam quis turpis in ante posuere luctus. Quisque dictum diam ac nulla imperdiet, a feugiat urna molestie. Nam id purus semper, lacinia lorem sed, bibendum lacus. Maecenas sit amet dui venenatis est porttitor ullamcorper vel.
                              
                """;
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be greater than 4000";

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }


    @Test
    public void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveOK() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.deactivate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActivated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.activate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory("Film", "A categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var aCategory =
                Category.newCategory("Film", "A categoria", true);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(aCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory("Filmes", "A categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final var actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(aCategory.getDeletedAt());
    }

}

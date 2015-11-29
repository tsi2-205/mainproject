package managedBeans;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import notifications.NotifyUserView;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

import comunication.Comunicacion;
import datatypes.DataCategory;
import datatypes.DataProductAdditionalAttribute;
import datatypes.DataStock;
import datatypes.DataStore;
import datatypes.DataUser;

@ManagedBean
@ViewScoped
public class NewProductBB implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
    private String description;
	private DataUser user;
	private DataStore store;
	private Integer stockMin = null;
	private Integer stockMax = null;
	private String imagenProducto;
	
	
	private DataStock stockSelected;
	private boolean isEdition = false;
	
	private TreeNode root;
	private TreeNode selectedNode;
	
	private List<DataProductAdditionalAttribute> additionalAttributes = new LinkedList<DataProductAdditionalAttribute>();
	
	private String additionalAttributeName = null;
	private String additionalAttributeValue = null;
	
	private DataProductAdditionalAttribute attributeSelected = null;
	
	private UploadedFile imagen;
	private File imagenFile;
	private boolean imagenCargada;
	private StreamedContent imagenStream;
	
	
	public NewProductBB() {
		super();
	}
	
	@PostConstruct
	private void init() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		ELContext contextoEL = context.getELContext( );
		Application apli  = context.getApplication( );	
		ExpressionFactory ef = apli.getExpressionFactory( );
		ValueExpression ve = ef.createValueExpression(contextoEL, "#{sessionBB}",SessionBB.class);
		SessionBB session = (SessionBB) ve.getValue(contextoEL);
		this.user = session.getLoggedUser();
		this.store = session.getStoreSelected();
		//System.out.print(store.getFile().length());
		this.stockSelected = session.getStockSelected();
		this.name = null;
		
		
		try {
			if (this.stockSelected != null) {
				int id = this.stockSelected.getProduct().getId();
				String im = Comunicacion.getInstance().getIStoreController().findImageProduct(id);
				if ( im.equals("") ){
					this.imagenProducto = "data:image/png;base64,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";
				}else{
					this.imagenProducto = Comunicacion.getInstance().getIStoreController().findImageProduct(id);
				}
			} else {
				this.imagenProducto = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALcAAAC3CAYAAABQbs+fAAAAAXNSR0IArs4c6QAAAVlpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IlhNUCBDb3JlIDUuNC4wIj4KICAgPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6dGlmZj0iaHR0cDovL25zLmFkb2JlLmNvbS90aWZmLzEuMC8iPgogICAgICAgICA8dGlmZjpPcmllbnRhdGlvbj4xPC90aWZmOk9yaWVudGF0aW9uPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KTMInWQAAGb1JREFUeAHt3Vdz5LYShuFZ55xzTmXvhX3p/3+tP+By2RcO65xzDscP7U+CaU2QRrtnCHRXQSQRGmj0iybImdm9dHR09OeqpGagwxm4rkObyqSagWkGCu4CodsZKLi7dW0ZVnAXA93OQMHdrWvLsIK7GOh2Bgrubl1bhhXcxUC3M1Bwd+vaMqzgLga6nYGCu1vXlmEFdzHQ7QwU3N26tgwruIuBbmeg4O7WtWVYwV0MdDsDBXe3ri3DCu5ioNsZKLi7dW0ZVnAXA93OQMHdrWvLsIK7GOh2Bgrubl1bhhXcxUC3M1Bwd+vaMqzgLga6nYGCu1vXlmEFdzHQ7QwU3N26tgwruIuBbmeg4O7WtWVYwV0MdDsDBXe3ri3DCu5ioNsZKLi7dW0ZVnAXA93OQMHdrWvLsIK7GOh2Bgrubl1bhhXcxUC3M1Bwd+vaMqzgLga6nYGCu1vXlmEFdzHQ7QwU3N26tgwruIuBbmeg4O7WtWVYwX3BDNx8882r66+/fvXnn39O6brrrpuuHcnvv/8+5V+6dGklT1L3t99+W/3yyy+rG2+8cXXDDTdM+epEzx9//LGSbrnlltXly5dXDz300Or7778/zlNXu5KTGajZOJmLCzn76aefjuENnBSDlIA/wOaYBaD8119/nRaARaD9rbfeurrrrrum5Bz8ANf2vffem476tKC0KTmZgYL7ZC4u5AxkJDA7iritgDaRPBFZ5HaeCHz77bevJGDfeeedE9Ctjptuuml12223rYCtreuCu52h1arg/vd87H0lsoIssLZRmfKAnqidDi0K6b777ptAvuOOOya451sN7SwAdR999NHVO++8M10n37Hk7xkouC+YBNsKYEsADNwB3rXzRGnbFCBLthv33HPPBGsie4Y3h1akBvcHH3wwVRHB5wshbUc9FtwX7PlE5oDtGsygl0R2yZYD0LYcjvbTcwF09AVu0IvcEZD/+OOP02KiN/VSPvKx4L5g74M6ETQRXBeJ0PbJInSiNThb8caEDinbD+UWiETUyUPl/fffPz1Yql9gT9Nz/KfgPp6KizkBNLiBmagtKj/44IMrIII6olx9kmg8hz11A7zrtAGzrclHH300bX/kz7czaT/iseCeeR1kbQR0HfBSlryApH6SrYdtgjpAfuqppyaoRe65aN/qAHu2Lm3d999/f4radBFRO6Kfxx9/fPXaa69Nb0+SX8d6W/IfBgAGONAEvERgR1E5IM8XAWUi78MPPzx9yGJfTWwnElWjU1v6HNNXG50//fTT1TfffDO96vvyyy+nPfkDDzwwAdyOkf5sc9rxyB9dKnLPCAAaSJJck2w17HeBKikDs4dC76NtP7zKy+LQDojqAZe0e+rk0fXDDz9MOr/66qvp+MUXX0x3AHUsDuP59ttvpz5cZ5HQaR+v388//9xlyT8zUHCvQQGQAVsVkEa8lQCyiAnsu+++ezq2wAFQ++QlelskgZq+n3/+eYJWlLadcaRffXWdq29RXLlyZdriZFw5qmNxWRAVveOl2paczMQ/Z3MoZQMmWwgREtiglgAYOQ3gtANooBapRWEg+36Id9Ta6oc+C0JynTxt1LMAROq5yHMXsVhK/p6BE8/UjEwzAECgAQlYgMlrO1D7kAWALdQagk/Kw14ifSI3XfI+/vjjSfd33303ge0BUh3Rl04ROqC3cCsDrm3LaXAneturl/w9AwX3jIRA5u2GcyDZdkiuWwmE4AzwAJZsGRKpRedPPvlk5cEwiyZbCotH+ywC16B2TY8+LADn9uWfffbZ9LCa+tFjbO4qBfeJhwruk7mYzoAlUnsnfe+99x5Dp1BkDnjtNiMqABhQRVlRGmyitHYWQIAPnNoqy0JRRz+BVr68LAJbGeX0ZAFp79w2qeRkBhYHN2cTzgwAJ+b8fZb8HDmfOIIqMAWiPBh6defDFnXA0wKoPciiy/Vc1AeytxagFqW1sZ0h+sv4LYTTRJQm6YcNtioSoctXXV966aXp2h91U887bw+e6dfeni3a2dLI3yStzXRmHI5SW75JzyGUbbb0EEY4G4PbbyY6RSacIyQCICABSB7naudoa0BEQrdxEVrEU6bdaR+2TA3++pPInGtHeaLp119/PW0bwA3QQKr/nKur331EP+xlR75kFX0WqcUJYmPStz6zqHbp3wMr/eZDe+fmW9ulyeLgNuGknfBEFGUcyTEc7VoZuPIe2ZZDhAZG3k1zYET96I5D42THwGLbYS8tMubhUHS1OERH/dOrvuRc+33fZojA7GOPbVMWsfFbwBaPxGZ2GI8jMaZt/atvnJHMhYXPDn0sRRYHd27PcYBjwDHpHgDVAR4HA0GeiMa5IjUHxkmcBxB61CX0Sa3kbiByAgTQAAss9NGb8amvf9egoFud9NHqPsu5sYquHix9YklfYM6csDHjY4c2xrlL/7GHriQ6LFjXbFmKLA7uNgqa9Dgg0cntmCjLr1hEaW87wBehR+I0SXsw0uc6AlKg0GsPbT+dvtRJ/xlL24cy4EUnEIG5j1ioAExiI2kXjYhuzFlY7ImtQN8kueMZM2Fr5spcsHMpcuLFhYw48LSTnslnguhkDy162VODi3AMODkvbZMPDM7UNgJCQIPEOfD1E7ADc46BIZGbHnnJ17/UQpi+znI0DoBKorf5kFzTbYzsYLf+LALlxu26Xbin9WuO2KQefdqlrfaZz9PaHlre4uA26SY5oHGECedQZc8999wElHyphZGTOU/9ODmwaK++hzFbGkdbEPVJ9GunbzAZB9EOCOmvhVpdKZLx5Pqsx+g2HgsvY4g9OYrwojpb0ibj3tQnfRL7WrvMmWvztRS5dHR0tKgf3XEUJ4GEIz0cug3beojKJp8TJHVPkwCnPJFUHqfmWvtWAuU6nSmnJ33Lo7PtJ/Va3Wc5T//0WHzsjrhr6DuAu1bPoj6LaON7Kt7IZHujPd3sW4ocbOTmxExm9o655izbDl8tzQcXJp1TRDQCqoAWYOWLSJyvzELQht60dSQ5ThfNn3X5qUIXSb30nevUO++x1eM5or2O7cmbX+vTvGZeEqHNh3GaZxFavvl11/K+PotTuyXJwcFtkgFiou0XOUhE5gBH2w6ijmTC46xENeVxSGCTRziOrji0baN8fi2vN4mN5tpCyPV8rpZu98HBLbLaL2ZvZ9/owRDY8jkCmBHXpzkleZxn3+kW7tWdn2Wddpu2QOiKo6O/x6MFDmy25sjOdl57sPuEkgOxRsQGMihNtj2l7UdENG8dksjDYdr6kAOo6rmtAtpbDw+Izi2UfBCiTbs46OpdsnjbeXNuTs1bT4AfHNyZZOCZdJE8ktdauc4TfSKQ/aIILVLnNV7uAOrkZ19p7yjCK5Pi8La8x/MW4NicO11P9h4c3HmgMckmPu+ZRVsAis4WQKAEuAR8yQMQoNXLXjz16aTfNVFO6NWPRdGjkycj//kDbHazOUcP5fJ7s/3g4DbBwPQgafIDHbiBmUjDV+r6xNA/bSBSg1odjgOw7Y0jPcBPFI+z1SP689rLd67ndVK3l6NAAGTz4u5ojp544onj+TZ/vcjBwQ1Ekw5mjgAt8OyV5ef1FgeAUrS1lwZqXo2pR4/yRKMsCpDbnsiPLs6WtOvJuadBal7Yar4sZPMCdHnOe5J/f1JxAJYlsph452BzDmIwApaA1FsP0APVOcfFYfI4jRPjNHmu1SM5OlfmOkegp51858roJ66NxRj1Y5yOriXlrrWhS92MTdn/KxlnbBS1jSlz1M7HZOTC/xwc3JnowCGi+G6Hr5YS+SQRVhT2NVbbl4sQ+vVJfyIcvfJBGliN093FopLvzQxQ0k6bwAJySZuSazcDBwc301uAAGbbYWsCoghQRG/lvvopWl4EPCCmR3IOUOfGJFlMQE00d0ykdmdJPeM0XuXqtwslNtTx6s7Awe25gQQGwIiE+cBFZPaaz/dIIuAjPiq23wbSvpIInSN9xgNaY3MH0Y/zSCA25gCunI6k2OW65NrMwMHBzflgIrnNu9UDylc87bGz3wZSIqMPevxD7PL2ERDSKRFQG5OxyLN39qlpvqilvjwPvsAHuPFnQWgLfvWkgnsf75yt7cHBneGDCiiAAjeAfMpIlBEAAR3Qovdbb7015e/zh259OtILRn0E0GeffXa6SwA849Bfvv/ttWQWgvHRxQ5g77vw9rFrxLYHuecGkj1q3oAADEigeffddydIAEPUAb69MOD2FRAS/QVO49GPu4N3wrZALdjqe7hU7tfnxmLMEh0E5K5Lrt0MHBzc4AIueLLfDsjK3n777Wl2kucCOMSW5SIEhHSCOlHYp3hPPvnkv2BNX8aS8eRfYg3c7MhCKLgzY9fmeHBwMzuAi8gRYIjmBEi5xYPP+1oiahIR3Ks59enQ1tYmAE6V1vyxBQmQ4HYuKvtGon7kGV+ApUY/kjJiEejTQ7C6hI5d4La9Ee2N2+LW3kKT9+qrr042yKNL0m/uDubEGNjumHz1nOd6GtAAfw4S7k3zzoE+JueoOBdoAcCDHqjAFMDV5ewWyHV9tOBmMbQABRB906mOoyTPtX4ssCyo3FnW9dnmG7dXn94KeUhlL72+x063BZz+6LcYJeOy0OVpk0WhrfHkuu2r9/PFwc15H3744TFInEYA5Nz/rAtqDuZsTpdSvs2hdEQnMALHfGGoQy/wA0/60YcPlow10G/rN+X0accGoHuIdicAu36efvrp4+/MiOwZo3bgz0I0PjZn/LlOPyMcFwc3p4hsXguCiXBgzj3s2UK4rXN0yudwTgWn/AEBfUR713RZKARAUurIU0ciwCSgTr6+1U+dqcKaP4C0MH3dQP9seeyxx6basUGeeurQyXZj1KejMnWV6TeQt2Ne031X2YuDmwM50/8TE2dzXsBR5q2FyBcIlANlF+fSow8CVH3YB1tQ2ruWAg3d6idp58MmW4qMVd/q7SLq2WbY33tv/sorrxyPmz4L65FHHpneyBiXeiJ8tiSBXF9Z8O3YdhlDL3UWB3dABRCAQRawOUW5f10qYO4C9Dpn0hvYgQS2wN0CDqJEebr8ctxiCNCpu8tYwJk9sy0WcOnPg67x2M8ra+FVrj/11XHuqG/nAHc9kiwObhCJXhzrhwkcF3g4jhPzThpgygJHYNjkYABqQ4Dm2kLRp/7obCV15enbP1ssagNJyhaGnl3gTn8gfeGFF463Q7FTH8Se3p3JAhe1XXtdKaprqz5d5itjLLinqTvcP5wlSnGsCBmHAZFwKMeK3kAEtDbKd4VbPXq0oSvv2315y3t2kBtDwM1seUuTMm3oaevQtU0sJOP23/JpL4obR4ANqGD2Y2dHtr788stTyr+JGLjVT5ttffdWfrAfv6+baLBwKLhELL/EcZsGk6gWZ4LBrdt2Ql1RzbZimyTaqgcsAnQCGG8vok8fJFuW7IHVc25RgdW1RByzeNhivOoR9ujrxRdfnGxKOzqIsaWta88WUkRb5d6uWIja0ylvRMAXBzeHcRSQOd0WANyEEwFDOFq+cvUD0lS4xx99gDv7fapAF/D0E5iMNf1mDBZMFkugZkvybCvclYi8FkrX+tkm9Frsxui81ZN+tunooXxxe27AEE4Cikjqth2nBQYQ+Dg+MAS6fZ0G3E0pi08dY5KMIeNIvnEAL9Cr582HD2lOgzv2bRu/euYldzf60zZj2Kajl/LFwc1RQOVAIIlO7XYjzgSRbYMvMWVBXIRzsw+n351Dcq4PZYB1boz6U+Zo3Dm2sKkrAdv3UrK/B1g73jxI7gKePm3D0md0tfp20bP0OouDm+OAAyr7SQ4TvZ1HAoIywDhqdxECXqI/C0tK38qy6IzRQkv9gKZu4DZOSZv5Pz6kD+2JOhZArqfMLX9EbgslfeW4pVlXxYuDu3Uwh4PHWwppLsptTURvgF+Eg/VvoUj6lnKtTJ/tYkqf8gjoI6DVHtjGmQWb8hzpSPvkrTumnjuBT2uzyOgwtpFkcXBznkhnC+BIQCJ6c54EGHmEk4HjOo6fCs75J29pwAJmybmIrMxrPBEz3+kW2ZUbk/6NTxsi39bJ67s8FKdMubrqZCHJ2ybas9Xc+D4KuLWnh76RZNFwi3QcBhr77taBXsVxKrH/DOz7Ohe4QCXASSSWp0y/xuWh0HgATzIWYw3AyhNh5VsgqadNxqxM2lUyJ8aQRd3Oza56ll5vcXCLhIAR8TiO00Ahkr/55pvH4Nlz5kEzX6ZSV5tEsBYY54Fuk1PVCyjqAzqLzBeZfLDiA5jnn39++l6I99DGJqJra1zGIM9P43zLz8Jgg2hLd1LuTLl23EW0s+hEbilfwmoXzi56ll5ncXDPIXQdpwGGJJpaAAHZrV+++tJpsgs8FhY4sxBEavAYgz6e/es3lrYkBPi+0Qdgd49Abky2IeAOwPIy7tPGdta8jE/0XmfvWXUurf5i4eYwMHJiIp7oCLZAKl9EJD4cUReEcXbqncVpINQ+8ARIsPp9Zd5R06kvkVO+5BNT7eV7iFQWCeS53ueoD+MzTuNyZ2Cr/JFkcZ9QclgLZ4AFsajqe94+fo4oF2nBY3vC0QGzhZvO9jrt50fRGJzq0ps9s+0HkORHl/IsJjADP1sY1+04cj7v7zzX7hDGRdjsPIv8PPqW2maRkdtkgygwtrD7CVpEOagTsUTvdXCnzbYjOIEoYtMLaJ8qgoikL2NSD1SJ7to+88wz01bF3ptEj/O0db6PZF7oAHbe3Oyjc4ltFwk3CFoQnIMJyPa/2ZqkDsjyACeSRloI5M2vU689isaB1Z5eBM4e252jjcDOAW1BGUvaAS4LUlnG5HxfYUN0R5cFKG+en/Jej4uDmyOAAjLCmYEbNADxK51sCdQJPGDLedo6nkUArE8PiO4E4I7QnbHNFwqw9G9cytpFkvYXcaTbHLT95y5RcF/EDF9lHQCSSEDhOFES4LYmgT97zTywtZG1BSC6JqUb/oi++hIN7bPtweVJoArEAUl++4ZFvmQhKGOHo7tNxryh+52K6G5tM0aSMe2kpINKi4vcnAYiIMdZjsAACJCUXblyZXIPqPOOGUjqxvEBPn5UBn7l9KV+8qLbqz1vP4g8MBkTSNXNuJTLB5cjyeJSx0J0rcwWhx75+6T0n370Sb/37sY6kuy/yTuw2QpYiYSASeTi8IC9adiBNZCDloDEvx+SV3jKA2LKt0XfjG9SeBX+6N+izcLURWxm10jSJdycKVr7hNKemFM5fRfnZhsD5LQDpIdGb0R8UBMBUBsh5c+vUzfHawH3vC99ukt4TvDAPYp0Bze4spf1zjsPfGeBKtFXG/okcOcLWKC2gFLmXJ60ywK6mnBlbI7GH7tF84L7as78NdAduO27/b7SNiKQglb5JtEuH+MHDItFxPOD5LxHB7Horg69gSlbgHV9ROe68n3z3XmAnDuV8ehTMh8jSXeRO3ABz3e8/VA2n0zuAneifrsIgEuXL20FYuX6olMCjz6Vb5KrDbexGEdszdgsWtu0vJPfNMZeyrqDmzNFLhA5+sfg8wC4DTxODQzap34LOp0t2OqJ4oFW+SZJvU119ikDdp4VjDPPEOAeCWxz2B3cgORIR29J3Io5GbSJ6pvg8eAVsBPFwQJax3z6qQ59dEv6A9I2eLeVbxrbLmXRb2zGFLtdZ8y76OmhTndwi1yJVuDm0Ndff311+fLlCcI4H7g5B4Bri8LWYy7KpIj6reQ6i6gt+3+cx359s1Hy9ogoyzcXbbV8etnaxwaLmFgc7RwpW5J0B7fJ5xCRKpHVA6IE/ES0OI4jJT8kdtuOY5fkxLOMFcjuTsRCZm++e5IoryyLAtA5V7ddOOodsnQHN2g5xJFwiK2E31j6PjVpIxBnA962g9PTbqrY4R8A57nAp6IkwUCZAEASBAK8Os4dlyLdwc0piTTOgSxCeWsSuJW3EVpUB3mi1FKcd55x5u6VtoHV0basFXPUzlMbNNp6h3reHdyZ6DjCEeT2lyK4qCUCyU8E41h1Anh09HhkN/sThdlN5AM5+amX7Zs5ykJYyrx0BzdncUgeLDlJRBKdvTnxhSd5cSpHBfKlOG2fcQZW8xNY181FFn22ao7mbimynJHuOKMcEgdymmQv7ZhP6DhIEqXiOOrb8x27W1w185PtRuahvW4Nkm8RWPwCxHzb0tY9xPPuIrdJbuF2zSm2JD6he+ONN6b334FbeaKYNwGJZvJ7lNhqYbPXojcXjuZNIHBtzjyn+NoBuCX5S5JLR0dHu/1jGEuyasNYObFk/Qz0tLiXtRTX+6RKagb+MwMF93+mpDJ6mYEu99ybnNPTbXeTnVX21+vNmoSagV5noCJ3r549p109PXBX5D4nBNXs8Geg4D58H9UIzzkDw21LerrtntPnwzSryD2Mq8cztOAez+fDWFxwD+Pq8QwtuMfz+TAWF9zDuHo8Qwvu8Xw+jMUF9zCuHs/Qgns8nw9jccE9jKvHM7TgHs/nw1hccA/j6vEMLbjH8/kwFhfcw7h6PEML7vF8PozFBfcwrh7P0IJ7PJ8PY3HBPYyrxzO04B7P58NYXHAP4+rxDC24x/P5MBYX3MO4ejxDC+7xfD6MxQX3MK4ez9CCezyfD2NxwT2Mq8cztOAez+fDWFxwD+Pq8QwtuMfz+TAWF9zDuHo8Qwvu8Xw+jMUF9zCuHs/Qgns8nw9jccE9jKvHM7TgHs/nw1hccA/j6vEMLbjH8/kwFhfcw7h6PEML7vF8PozFBfcwrh7P0IJ7PJ8PY3HBPYyrxzO04B7P58NYXHAP4+rxDC24x/P5MBYX3MO4ejxDC+7xfD6Mxf8DZ8Lhog2nuHAAAAAASUVORK5CYII=";
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		this.imagen = null;
		this.imagenFile = null;
		this.imagenStream = null;
    	this.imagenCargada = false;
    	
		this.description = null;
		if (this.stockSelected != null) {
			this.isEdition = true;
			this.name = this.stockSelected.getProduct().getName();
			this.description = this.stockSelected.getProduct().getDescription();
			this.stockMin = this.stockSelected.getCantidadMin();
			this.stockMax = this.stockSelected.getCantidadMax();
			this.additionalAttributes = this.stockSelected.getProduct().getAdditionalAttributes();
		}
		this.constructCategoryTree();
	}
	
	
	
	public void subirImagen(FileUploadEvent event) {
		try {
			Comunicacion.getInstance().getIProductController().guardarImagenProducto(event.getFile().getInputstream());
			this.imagenCargada = true;
			this.imagenFile = Comunicacion.getInstance().getIProductController().asociarImagen("nombreNuevo");
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UploadedFile getImagen() {
		return imagen;
	}

	public void setImagen(UploadedFile imagen) {
		this.imagen = imagen;
	}
	
	public StreamedContent getImagenStream() {
		try {
			if (this.imagenFile != null) {
				if (this.imagenFile.exists()){
//					this.imagenStream = new DefaultStreamedContent(new FileInputStream(this.imagenFile));
					this.imagenStream = new DefaultStreamedContent(new FileInputStream(new File(this.imagenFile.getAbsolutePath())));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return imagenStream;
	}

	public void setImagenStream(StreamedContent imagenStream) {
		this.imagenStream = imagenStream;
	}

	public boolean isImagenCargada() {
		return imagenCargada;
	}

	public void setImagenCargada(boolean imagenCargada) {
		this.imagenCargada = imagenCargada;
	}
	
	
	public File getImagenFile() {
		return imagenFile;
	}

	public void setImagenFile(File imagenFile) {
		this.imagenFile = imagenFile;
	}

	public String addAttribute() {
		String ret = "OkAddAttribute";
		DataProductAdditionalAttribute dpaa = new DataProductAdditionalAttribute(this.additionalAttributeName, this.additionalAttributeValue);
		this.additionalAttributeName = null;
		this.additionalAttributeValue = null;
		this.additionalAttributes.add(dpaa);
		return ret;
	}
	
	public void deleteAttributeSelectedCreate() {
		this.additionalAttributes.remove(this.attributeSelected);
		this.attributeSelected = null;
	}
	
	public void deleteAttributeSelectedEdition() {
		this.stockSelected.getProduct().getAdditionalAttributes().remove(this.attributeSelected);
		this.attributeSelected = null;
	}
	
	public String create() {
		String ret = "OkNewProduct";
		
		try {
			if (!isEdition) {
				if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
					Comunicacion.getInstance().getIProductController().createSpecificProduct(this.name, this.description,this.stockMin, this.stockMax, this.store.getId(),this.additionalAttributes, ((DataCategory) selectedNode.getData()).getId() , this.imagenProducto);

					if (Comunicacion.getInstance().getIProductController().shouldPromoteProduct(this.name)) {
						String message = "Se sugiere promover el producto " + this.name + " como producto generico";
						Comunicacion.getInstance().getINotificationController().sendAdminNotification(message);
						List<DataUser> administrators = Comunicacion.getInstance().getIUserController().getAdministrators();
						NotifyUserView notifyView = new NotifyUserView();
						for (DataUser admin : administrators) {
							notifyView.sendNotification(admin.getId(), message);
						}
					}
					
				} else {
					FacesMessage msg = new FacesMessage("Debe seleccionar la categroía en la que se va a incluir el producto");
			        FacesContext.getCurrentInstance().addMessage(null, msg);
			        ret = "FailNewProduct";
				}
			} else {
				this.stockSelected.getProduct().setName(this.name);
				this.stockSelected.getProduct().setDescription(this.description);
				this.stockSelected.getProduct().setImage(this.imagenProducto);
				this.stockSelected.setCantidadMin(this.stockMin);
				this.stockSelected.setCantidadMax(this.stockMax);
				
				this.stockSelected.getProduct().setAdditionalAttributes(this.additionalAttributes);
				if (this.stockSelected.getProduct().isGeneric()) {
					if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
						this.stockSelected.setCantidadMax(this.stockMax);
						this.stockSelected.setCantidadMin(this.stockMin);
						Integer idCategory = ((DataCategory) selectedNode.getData()).getId();
						Comunicacion
								.getInstance()
								.getIProductController()
								.editProductStore(this.stockSelected, store.getId(), idCategory, this.imagenProducto);
					} else {
						FacesMessage msg = new FacesMessage("Debe seleccionar la categroía en la que se va a incluir el producto");
				        FacesContext.getCurrentInstance().addMessage(null, msg);
				        ret = "FailNewProduct";
					}
				} else {
					this.stockSelected.setCantidadMax(this.stockMax);
					this.stockSelected.setCantidadMin(this.stockMin);
					Integer idCategory = null;
					if ((selectedNode != null) && (((DataCategory) selectedNode.getData()).getId() != -1)) {
						idCategory = ((DataCategory) selectedNode.getData()).getId();
					}
					Comunicacion
							.getInstance()
							.getIProductController()
							.editProductStore(this.stockSelected, store.getId(), idCategory, this.imagenProducto);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void constructCategoryTree() {
		List<DataCategory> categories = new LinkedList<DataCategory>();
		try {
    		categories = Comunicacion.getInstance().getICategoryController().findSpecificCategoriesStore(store.getId());
		} catch (NamingException e) {
			e.printStackTrace();
		}
    	this.root = new DefaultTreeNode(new DataCategory(-2, "root", "", false), null);
    	this.root.setExpanded(true);
    	TreeNode raiz = new DefaultTreeNode(new DataCategory(-1, "CATEGORÍAS", "", false), this.root);
    	raiz.setExpanded(true);
    	for (DataCategory dCat: categories) {
    		constructNodeTree(dCat, raiz);
    	}
    	
    }
    
    public void constructNodeTree(DataCategory dCat, TreeNode nodoPadre) {
    	TreeNode nodo = new DefaultTreeNode(dCat, nodoPadre);
    	for (DataCategory dCatSon: dCat.getSonsCategories()) {
    		constructNodeTree(dCatSon, nodo);
    	}
    }
    
    public String seeGenericProducts() {
    	return "/pages/ListGenericProducts.xhtml?faces-redirect=true";
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DataUser getUser() {
		return user;
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		this.store = store;
	}

	public Integer getStockMin() {
		return stockMin;
	}

	public void setStockMin(Integer stockMin) {
		this.stockMin = stockMin;
	}

	public Integer getStockMax() {
		return stockMax;
	}

	public void setStockMax(Integer stockMax) {
		this.stockMax = stockMax;
	}

	public String getImagenProducto() {
		return imagenProducto;
	}
	
	public void setImagenProducto( String imagenProducto) {
		this.imagenProducto = imagenProducto;
	}
	
	public String getAdditionalAttributeName() {
		return additionalAttributeName;
	}

	public void setAdditionalAttributeName(String additionalAttributeName) {
		this.additionalAttributeName = additionalAttributeName;
	}

	public String getAdditionalAttributeValue() {
		return additionalAttributeValue;
	}

	public void setAdditionalAttributeValue(String additionalAttributeValue) {
		this.additionalAttributeValue = additionalAttributeValue;
	}

	public List<DataProductAdditionalAttribute> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(
			List<DataProductAdditionalAttribute> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public boolean isEdition() {
		return isEdition;
	}

	public void setEdition(boolean isEdition) {
		this.isEdition = isEdition;
	}

	public DataStock getStockSelected() {
		return stockSelected;
	}

	public void setStockSelected(DataStock stockSelected) {
		this.stockSelected = stockSelected;
	}

	public DataProductAdditionalAttribute getAttributeSelected() {
		return attributeSelected;
	}

	public void setAttributeSelected(
			DataProductAdditionalAttribute attributeSelected) {
		this.attributeSelected = attributeSelected;
	}
	
}
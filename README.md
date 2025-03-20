### Summary

This project was developed in the context of a technical challenge. The purpose is to load data from this *[link](https://static.leboncoin.fr/img/shared/technical-test.json)*. The structure of the objects returned in the list is as follows:
 ```
 {
    "albumId": 1,
    "id": 1,
    "title": "accusamus beatae ad facilis cum similique qui sunt",
    "url": "https://placehold.co/600x600/92c952/white/png",
    "thumbnailUrl": "https://placehold.co/150x150/92c952/white/png"
  }
```
For the purpose of this challenge we should present at least the title (```title```) and the image (which we can get from ```url```).  

Furthermore, the app should work offline which implies that we should save the data objects into our app's database so we can fetch the data from there and not from the API which needs internet connection. 


### Structure

For this project I applied the architecture I'm most comfortable and familiarized with which is MVVM. This also relates to best practices in mobile development and provides a comprehensive structure for the project. In this app a scrollable list is presented, this list contains cards with the title associated to the object and the preview image. Whenever we press one of the cards in the list a dialog is presented with the title and the image from the url provided in the object. The following sections present in more detail the implementation for the several components of this project. 
#### Views:
 - MainActivity
	 - MainScreen
		 - MyScrollableList
			 - ListPhotoComponent
				 - CustomDialog

A ```MainActivity``` is necessary to initialize the views we want to show. In the ```MainScreen``` we implement a ```Scaffold``` component as to better structure our UI, divided in ```TopAppBar``` and the body. The body of the ```MainScreen```contains a component called ```MyScrollableList```which renders the items fetched from the API provided. We use ```LazyColumn```to provide scrollable capabilities and  for each item of the list we present a new component named ```ListPhotoComponent```which presents the data of the object in a card. A dialog was also implemented  to show the image we have in the ```url``` parameter of the object, since we show the ```thumbnailURl```in the ListPhotoComponent. We present the title of the object both in the card and in the dialog when we select one of the objects. 

We used ```AsyncImage``` to present the images from the urls provided since it's an async process and we wouldn't want to lock the UI while waiting for all the images to load. Therefore we use this component and whenever they finish loading they are presented.


#### ViewModel
For this challenge a single viewModel is sufficient and it was implemented under the name ```PhotoViewModel``` we also use a ```PhotoViewModelFactory``` to allow us to cleanly create the viewModel instance in our activity. In our viewModel we communicate with the repository to fetch and save the data from the API to the database and get the data stored in the database to be presented in the view.

#### Repository
For the repository only two methods were needed, one (```fetchAndStorePhotos()```) to get the data from the API and store it in the database, and another (```getPagedPhotos()```) to get the data from the database as a ```PagingSource```. The decision to use a ```Pager```came up due to concerns of performance. Since the list returned from the API is very long, rendering all the objects becomes very taxing and could slow down the app. Therefore, we implemented a Pager which allows us to only render a given amount (20) at a time saving resources and ensuring better performance. The last method mentioned in the repository returns a flow which is then captured in the viewModel and is used in the view component with ```collectAsLazyPagingItems``` to be used in the lazy column component.

#### API Calls
Using Retrofit we perform our API calls, having the base URL be "https://static.leboncoin.fr/img/shared/". We build our api object using ```Retrofit.Builder()``` and in our ```APIService``` we get the information from the ```technical-test.json``` endpoint.


#### Database
For our database we have a **Photo** data class that represents the objects fetched from the API to be saved into our database as such:
```
@Entity(tableName = "photos")  
data class Photo(  
    val albumId: Int,  
    @PrimaryKey val id: Int,  
    val title: String,  
    val url: String,  
    val thumbnailUrl: String  
)
```
This entity is then used in our Dao (```PhotoDao```), in the method to ```insertPhotos``` into our database which receives a list of the previously mentioned Photo entity. If there are conflicts (Photo object that already exists), the older record is replaced for the most recently fetched. In our method to get the data from the database as a PagingSource we  return ```PagingSource<Int, Photo>```. 

#### Unit tests 
To test the functionality of our app I implemented unit tests for both the viewModel methods and both the repository methods. This led me to attain test coverage of 100% in the viewModel and 66% of the methods in the repository (the init was not tested). Furthermore, these tests allowed to properly understand that the data circulates properly through the app. The mockito lib was used to mock classes needed as dependencies in repository and viewModel classes, the ```whenever```method was used to mock the result of method calls and the ```verify``` to assure that a method was called. In some cases the ```assertEquals```was used as well to validate the value of the obtained results was what we expected.




### Summary

This project was developed in the context of a technical challenge. The goal was
to load data from the following *[link](https://static.leboncoin.fr/img/shared/technical-test.json)*. The API returns a list of objects with the following structure:
 ```
 {
    "albumId": 1,
    "id": 1,
    "title": "accusamus beatae ad facilis cum similique qui sunt",
    "url": "https://placehold.co/600x600/92c952/white/png",
    "thumbnailUrl": "https://placehold.co/150x150/92c952/white/png"
  }
```
The app is required to:

- Display the title (`title`) and image (`url`) for each object.
- Work offline by persisting the fetched data into a local database, enabling retrieval without network connectivity.

### **Architecture and Structure**

The project follows the **Model-View-ViewModel (MVVM)** architecture, a popular and well-suited pattern for Android development. This ensures clear separation of concerns, testability, and maintainability.

#### **Application Flow**

The app displays a scrollable list of photos. Each list item shows the title and thumbnail image. When a user taps on a photo, a dialog appears with the full-sized image and its title. Below is the breakdown of key components:

1. **`AlbumsApp`**: Defines `HiltAndroidApp` for dependency injection.
2. **`MainActivity`**: Serves as the entry point for initializing the views.
3. **`AlbumsScreen`**: Implements a `Scaffold` with a loading spinner (`CircularProgressIndicator`) while fetching data. Once complete, it displays the list of photos.
4. **Components**:
    - `PhotoList`: Displays the list of photos using a `LazyColumn`.
    - `PhotoItem`: Represents individual list items with the title and thumbnail image.
    - `CustomDialog`: Displays the full-size image and title upon interaction.

#### **Performance Considerations**:

- Used `LazyColumn` for efficient rendering of large lists.
- Implemented pagination via `PagingSource` to load items in small chunks.

#### ViewModel
For this challenge a single viewModel is sufficient and it was implemented under the name ```AlbumsViewModel```. In our viewModel we communicate with the repository to fetch and save the data from the API to the database and get the data stored in the database to be presented in the view.

#### Repository
For the repository only two methods were needed, one (```fetchAndStoreAlbums()```) to get the data from the API and store it in the database, and another (```getPagedPhotos()```) to get the data from the database as a ```PagingSource```. The decision to use a ```Pager```came up due to concerns of performance. Since the list returned from the API is very long, rendering all the objects becomes very taxing and could slow down the app. Therefore, a Pager was implemented which allows us to only render a given amount (20) at a time saving resources and ensuring better performance. The last method mentioned in the repository returns a flow which is then captured in the viewModel and is used in the view component with ```collectAsLazyPagingItems``` to be used in the lazy column component.

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


## Choices made

#### Hilt for Dependency Injection

Using Hilt provides a clear and concise way of managing dependencies by automatically generating the required code. It reduces boilerplate code and complexity. By using ```@Inject``` and ```@Module```dependencies are explicitly defined and easy to trace improving code readability. 


#### AsyncImage (Coil)

This provides efficient image rendering and caching for android applications. Reduces network usage and improves performance. Coil's caching mechanism avoids re-downloading the same images repeatedly making it suitable for applications with frequent image loading.

#### Pager 

Pagers load data in chunks, which is particularly useful for handling large datasets like databases, reducing memory usage. By using Pager, the application provides a smooth infinite scrolling experience to users while ensuring optimal resource optimization. 


#### Room

Room integrates seamlessly with Kotlin Coroutines and Flows, making it easy to observe changes in the database and update the UI reactively. It also ensures that data is persistently stored locally allowing the application to function offline or in low-connectivity scenarios.


#### Retrofit

Retrofit automatically parses JSON responses into Kotlin objects, reducing manual parsing effort. It also works seamlessly with Kotlin Coroutines, enabling non-blocking API calls and handling background tasks efficiently.

#### Mockito (Unit Tests)

Mockito allows creating mock objects for dependencies which is essential for testing classes in isolation. By mocking dependencies you can focus on testing the behavior of the class under test. By mocking external dependencies like APIs or databases, tests can be run independently of the network or local database, ensuring consistency and speed.





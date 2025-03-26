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
The app displays a scrollable list of photos. Each list item shows the title and thumbnail image. When a user taps on a photo, they navigate to another screen with more details about the photo they selected. Below is the breakdown of key components:

1. **`AlbumsApplication`**: Defines `HiltAndroidApp` for dependency injection.
2. **`MainActivity`**: Serves as the entry point for initializing the views.
3. **AlbumsApp**: Component that creates the navigation component and starts the navigation flow.
4. **`AlbumsScreen`**: Implements a `Scaffold` with a loading spinner (`CircularProgressIndicator`) while fetching data. Once complete, it displays the list of photos or a warning because there are no photos saved locally.
5. **`AlbumsViewModel`**: ViewModel paired with the AlbumsScreen which loads the data to be presented in the UI.
6. A snackbar is also used to alert the user they are not connected to the internet.
7. **Components**:
    - `PhotoList`: Displays the list of photos using a `LazyColumn`.
    - `PhotoItem`: Represents individual list items with the title and thumbnail image.
    - `Warning`: Warning screen presented when the list of Photos is empty
    - `PhotoDetailScreen`: Screen which presents additional information about the selected photo.
8. **`PhotoDetailViewModel`**: ViewModel paired with the **`PhotoDetailScreen`** to load the data to be presented in this screen.

#### **Performance Considerations**:
- Used `LazyColumn` for efficient rendering of large lists.
- Implemented pagination via `PagingSource` to load items in small chunks.

#### ViewModel
##### `AlbumsViewModel`
 In our viewModel we communicate with the repository to fetch and save the data from the API to the database and get the data stored in the database to be presented in the view.

##### `PhotoDetailViewModel`
In this viewModel we communicate with the repository to get the selected photo from the database and add it to our ```uiState``` to be presented in the corresponding view.

#### Repository
The repository implements several key methods to manage data efficiently. In the `init` block, we register with the `networkMonitor` to enable network status tracking. This allows us to perform a specific callback action, which involves fetching data from the API and saving it to the database.
Additionally, the repository includes a method for updating the value of a private local variable. This variable ensures that API calls are not triggered unnecessarily—such as when there is no network connection or if an API call has already been performed previously (from the ViewModel).
The `fetchAndStoreAlbums` method is responsible for making the API call and persisting the fetched data into the database. For improved performance, the `getPagedPhotos` method returns a pager rather than the entire list. This approach optimizes memory usage and is particularly suitable for large datasets. The data from the pager is collected in the ViewModel and subsequently passed to the composable that manages and displays the captured photos.
Lastly, the `getPhotoByPhotoId` method interacts with the DAO to retrieve a single photo based on the provided photo ID.

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
This entity is then used in our Dao (```PhotoDao```), in the method to ```insertPhotos``` into our database which receives a list of the previously mentioned Photo entity. If there are conflicts (`Photo` object that already exists), the older record is replaced for the most recently fetched. In our method to get the data from the database as a PagingSource we  return ```PagingSource<Int, Photo>```. 

#### Unit tests 
To test the functionality of our app unit tests were implemented for both the viewModel method and both the repository methods. This led me to attain test coverage of 100% in the viewModel and 66% of the methods in the repository (the init was not tested). Furthermore, these tests allowed to properly understand that the data circulates correctly through the app. The mockito lib was used to mock classes needed as dependencies in repository and viewModel classes, the ```whenever```method was used to mock the result of method calls and the ```verify``` to assure that a method was called. In some cases the ```assertEquals```was used as well to validate the value of the obtained results was what we expected.

## Choices made

#### Hilt for Dependency Injection
Using Hilt provides a clear and concise way of managing dependencies by automatically generating the required code. It reduces boilerplate code and complexity. By using ```@Inject``` and ```@Module```annotations are explicitly defined and easy to trace improving code readability. 

#### AsyncImage (Coil)
This provides efficient image rendering and caching for android applications. Reduces network usage and improves performance. Coil's caching mechanism avoids re-downloading the same images repeatedly making it suitable for applications with frequent image loading.

#### Pager
Pagers load data in chunks, which is particularly useful for handling large datasets like databases, reducing memory usage. By using Pager optimal resource usage is accomplished.

#### LazyColumn
This provides a smooth infinite scrolling experience to users and ensures some performance enhancements since only the visible items on the screen and a few off-screen ones are composed and rendered. This lazy loading ensures better performance and lower memory consumption, especially when dealing with large datasets. This composable used with the `AsyncImage`ensures optimal resource management and usage.

#### Room
Room integrates seamlessly with Kotlin Coroutines and Flows, making it easy to observe changes in the database and update the UI reactively. It also ensures that data is persistently stored locally allowing the application to function offline or in low-connectivity scenarios.

#### Retrofit
Retrofit automatically parses JSON responses into Kotlin objects, reducing manual parsing effort. It also works seamlessly with Kotlin Coroutines, enabling non-blocking API calls and handling background tasks efficiently.

#### Mockito (Unit Tests)
Mockito allows creating mock objects for dependencies which is essential for testing classes in isolation. By mocking dependencies you can focus on testing the behavior of the class under test. Additionally mocking external dependencies like APIs or databases, tests can be run independently of the network or local database, ensuring consistency and speed.





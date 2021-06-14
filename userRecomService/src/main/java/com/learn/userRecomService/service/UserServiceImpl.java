package com.learn.userRecomService.service;

import com.learn.userRecomService.model.TMDBData;
import com.learn.userRecomService.model.TMDBMovies;
import com.learn.userRecomService.model.UserRecom;
import com.learn.userRecomService.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService{


    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public List<UserRecom> getAllUsers() {
        List<UserRecom> users = userRepo.findAll();
        if(users.isEmpty()){
            return new ArrayList<>();
        }
        return users;
    }

    @Override
    public UserRecom saveUser(UserRecom userRecom) {
        return userRepo.save(userRecom);
    }

    @Override
    public List<UserRecom> getAuthUser(String email) {
        List<UserRecom> favList = new ArrayList<>();
        Optional<List<UserRecom>> user = userRepo.findByUserEmailAndIsFavourite(email,"true");

        if(!user.get().isEmpty()){
            user.get().stream().forEach(ele->{
                if(favList.isEmpty()){
                    favList.add(ele);
                }
                else{
                    if(!favList.stream().anyMatch(item->item.getMovieId().equalsIgnoreCase(ele.getMovieId()))){
                        favList.add(ele);
                    }

                }
            });
            return favList;
        }
//        throw new UserWithGivenEmailNotExistException("User with given email doesn't exist.");
        return new ArrayList<>();
    }

    @Override
    public List<UserRecom> getAllComments(String movieId) {
        Optional<List<UserRecom>> byMovieId = userRepo.findByMovieId(movieId);
        if(byMovieId.get().isEmpty()){
            return new ArrayList<>();
        }
        return byMovieId.get().stream().filter(ele->ele.getComment().length()!=0).collect(Collectors.toList());
    }

    @Override
    public void setComment(String email, UserRecom userRecom) {
        if(userRecom.getId().length()!=0){
            userRecom.setUserEmail(email);
            userRepo.save(userRecom);
        }
            userRecom.setUserEmail(email);
            userRepo.save(userRecom);
    }

    @Override
    public void setLike(String email, UserRecom userRecom) {

        List<UserRecom> existList = userRepo.findByUserEmailAndMovieId(email, userRecom.getMovieId()).get();
        if(!existList.isEmpty()){
            existList.forEach(ele->{
                ele.setIsLiked(userRecom.getIsLiked());
                userRepo.save(ele);
            });
        }
        else{
            userRecom.setUserEmail(email);
            userRepo.save(userRecom);
        }

    }

    @Override
    public TMDBData getRandomMovies(String email, TMDBData randomData) {
        randomData.getResults().stream().forEach(ele->{
            List<UserRecom> list = new ArrayList<>();
            List<UserRecom> likeData = userRepo.findByIsLikedAndMovieId("true", ele.getId()).get();
            if(!likeData.isEmpty()){
                likeData.forEach(item->{
                    if(list.isEmpty()){
                        list.add(item);
                    }
                    else{
                        if (!list.stream().anyMatch(data->data.getUserEmail().equalsIgnoreCase(item.getUserEmail()))){
                            list.add(item);
                        }
                    }
                });
            }
            List<UserRecom> userRecoms = userRepo.findByUserEmailAndMovieId(email, ele.getId()).get();
            if(!userRecoms.isEmpty()){
                UserRecom userRecom = userRecoms.get(0);
                ele.setIsLiked(userRecom.getIsLiked());
                ele.setIsFav(userRecom.getIsFavourite());
                ele.setIsRecommended(userRecom.getIsRecommended());
                if (list.isEmpty()&&list.size()==0){
                    ele.setLikeCount("");
                }else{
                    ele.setLikeCount(list.size()+"");
                }
            }
            else {
                ele.setIsLiked("false");
                if (list.isEmpty()){
                    ele.setLikeCount("");
                }else{
                    ele.setLikeCount(list.size()+"");
                }
                ele.setIsRecommended("false");
                ele.setIsFav("false");
            }

        });
        return randomData;
    }

    @Override
    public void setFav(String email, UserRecom userRecom) {
        List<UserRecom> existList = userRepo.findByUserEmailAndMovieId(email, userRecom.getMovieId()).get();
        if(!existList.isEmpty()){
            existList.forEach(ele->{
                ele.setIsFavourite(userRecom.getIsFavourite());
                userRepo.save(ele);
            });
        }
        else{
            userRecom.setUserEmail(email);
            userRepo.save(userRecom);
        }
    }

    @Override
    public void deleteFav(String email,String movieId) {
        List<UserRecom> userRecoms = userRepo.findByUserEmailAndMovieId(email, movieId).get();
        userRecoms.forEach(ele-> {
            ele.setIsFavourite("false");
            userRepo.save(ele);
        } );
    }

    @Override
    public List<TMDBMovies> getTrending(String userEmail) {
        List<UserRecom> all = userRepo.findAll();
        List<UserRecom> existingUserData = userRepo.findByUserEmailAndIsLiked(userEmail, "true").get();
        List<UserRecom> custom = new ArrayList();

        if(!all.isEmpty()){
            Map<String, Integer> collect=new HashMap<>();
            all.stream().filter(ele-> ele.getIsLiked().equalsIgnoreCase("true"))
                    .forEach(item->{
                        if(custom.isEmpty()){
                            custom.add(item);
                        }
                        else{
                            if(!custom.stream().anyMatch(ele->ele.getUserEmail().equalsIgnoreCase(item.getUserEmail())&&ele.getMovieId().equalsIgnoreCase(item.getMovieId()))){
                                custom.add(item);
                            }
                        }
                    });
            Map<String,Integer> map = new HashMap<>();
            if(!custom.isEmpty()){
                custom.stream().forEach(ele->{
                    if(map.isEmpty()){
                        map.put(ele.getMovieId(),1);

                    }
                    else {
                        if(map.containsKey(ele.getMovieId())){
                            map.put(ele.getMovieId(),map.get(ele.getMovieId())+1);
                        }
                        else {
                            map.put(ele.getMovieId(),1);
                        }
                    }
                });
            }

            if (!map.isEmpty()){
                collect = map.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(10)
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
            }

            List<UserRecom> trendingMovies = new ArrayList<>();

            if(!collect.isEmpty()&&!custom.isEmpty()){
                collect.keySet().stream().forEach(item->{
                    UserRecom trendingOrder = custom.stream().filter(ele -> ele.getMovieId().equalsIgnoreCase(item)).findFirst().get();
                    trendingMovies.add(trendingOrder);
                });
            }

            List<TMDBMovies> tmdbTrending = new ArrayList<>();
            Map<String,Integer> collectCollect=collect;
            if(!trendingMovies.isEmpty()){
                trendingMovies.stream().forEach(ele->{
                    String isLiked="false";
                    String likeCounts = collectCollect.get(ele.getMovieId()).toString();
                    if((!existingUserData.isEmpty())&&existingUserData.stream().anyMatch(item->item.getMovieId().equalsIgnoreCase(ele.getMovieId()))){
                        isLiked="true";
                    }
                    tmdbTrending.add(new TMDBMovies(ele.getMovieId(),ele.getTitle(),ele.getRating(),ele.getDescription(),ele.getPosterUrl(),isLiked,likeCounts,ele.getIsFavourite(),ele.getIsRecommended()));
                });
            }

            if(!tmdbTrending.isEmpty()){
                List<TMDBMovies> sorted = tmdbTrending.stream().sorted(Comparator.comparing(TMDBMovies::getLikeCount).reversed()).collect(Collectors.toList());
                return sorted;
            }

            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    public List<TMDBMovies> getSearchedMovie(TMDBData searchedMovies, String email,String title) {
        Optional<TMDBMovies> optionalTmdbMovies = searchedMovies.getResults().stream().filter(ele -> ele.getTitle().equalsIgnoreCase(title)).findFirst();
        if(optionalTmdbMovies.isEmpty()){
            return new ArrayList<>();
        }
        else{
            TMDBMovies tmdbMovies = optionalTmdbMovies.get();
            List<UserRecom> userRecoms = userRepo.findByUserEmailAndMovieId(email, tmdbMovies.getId()).get();
            List<UserRecom> userRecom = userRecoms.stream().filter(ele -> ele.getIsLiked().equalsIgnoreCase("true")).collect(Collectors.toList());
            if(!userRecom.isEmpty()){
                tmdbMovies.setIsLiked("true");
            }
            else{
                tmdbMovies.setIsLiked("false");
            }

            List<UserRecom> all = userRepo.findByIsLikedAndMovieId("true", tmdbMovies.getId()).get();
            List<UserRecom> countList = new ArrayList<>();

            all.stream().forEach(ele->{
                if(countList.isEmpty()){
                    countList.add(ele);
                }
                else{
                    if(!countList.stream().anyMatch(item->item.getMovieId().equalsIgnoreCase(ele.getMovieId())&&item.getUserEmail().equalsIgnoreCase(ele.getUserEmail()))){
                        countList.add(ele);
                    }
                }
            });

            if(!countList.isEmpty()){
                tmdbMovies.setLikeCount(countList.size()+"");
            }
            else{
                tmdbMovies.setLikeCount("");
            }
            return List.of(tmdbMovies);
        }
    }

}

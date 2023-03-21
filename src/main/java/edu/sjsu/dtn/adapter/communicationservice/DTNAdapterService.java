package edu.sjsu.dtn.adapter.communicationservice;

import com.google.protobuf.ByteString;
import edu.sjsu.dtn.storage.FileStoreHelper;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class DTNAdapterService extends DTNAdapterGrpc.DTNAdapterImplBase{
    private static final String ROOT_DIRECTORY = "C:\\Users\\dmuna\\Documents\\java\\DTN-bundle-server-adapter\\FileStore";
    @Override
    public void saveData(AppData request, StreamObserver<Status> responseObserver) {
        FileStoreHelper helper = new FileStoreHelper(ROOT_DIRECTORY + "/receive");
        helper.AddFile(request.getClientId(), request.getData(0).toByteArray());
        responseObserver.onNext(Status.newBuilder().setCode(0).build());
        responseObserver.onCompleted();
    }

    @Override
    public void getData(ClientData request, StreamObserver<AppData> responseObserver) {
        FileStoreHelper helper = new FileStoreHelper(ROOT_DIRECTORY + "/send");
        List<byte[]> dataList = helper.getAppData(request.getClientId());
        List<ByteString> dataListConverted = new ArrayList<>();
        for (int i=0;i<dataList.size();i++){
            dataListConverted.add(ByteString.copyFrom(dataList.get(i)));
        }
        //
        AppData appData = AppData.newBuilder()
                .addAllData(dataListConverted)
                .build();
        responseObserver.onNext(appData);
        responseObserver.onCompleted();
    }

    @Override
    public void prepareData(ClientData request, StreamObserver<Status> responseObserver) {
        responseObserver.onNext(Status.newBuilder().setCode(0).build());
        responseObserver.onCompleted();
    }
}
